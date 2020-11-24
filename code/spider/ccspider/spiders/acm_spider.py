import re
import urllib.parse as urlparse
from datetime import datetime

import scrapy
from scrapy import Request

from ccspider.items import CcspiderItem
from ccspider.utils import DateRange


def _remove_html(string):
    pattern = re.compile(r'<[^>]+>')
    return (re.sub(pattern, '', string).replace('\n', '').replace('  ', '')).strip()


class ACMSpider(scrapy.Spider):
    name = 'acm'
    allowed_domains = ['dl.acm.org']

    def __init__(self, start_year=2020, end_year=2020, before_month=1, after_month=1, page_size='50', **kwargs):
        super().__init__(**kwargs)

        self.start_year = int(start_year)
        self.end_year = int(end_year)
        self.before_month = int(before_month)
        self.after_month = int(after_month)

        # check arguments
        if self.before_month < 1 or self.before_month > 12:
            raise ValueError('before_month is not a valid month')
        elif self.after_month < 1 or self.after_month > 12:
            raise ValueError('after_month is not a valid month')

        if self.start_year > self.end_year:
            raise ValueError('Not a valid time range: start_year is larger than end_year.')
        elif self.start_year == self.end_year and self.before_month < self.after_month:
            raise ValueError('Not a valid time range: month range is not valid.')

        now = datetime.now()
        if self.end_year > now.year:
            raise ValueError('end_year is larger than this year.')

        self.start_page = 0
        self.base_url = 'https://dl.acm.org/action/doSearch?'
        self.params = {
            'fillQuickSearch': 'false',
            'field1': 'AllField',
            'ContentItemType': 'research-article',
            'expand': 'dl',
            'startPage': '0',
            'pageSize': page_size}

    def start_requests(self):
        for year, month in DateRange(self.start_year, self.after_month, self.end_year, self.before_month):
            self.params['AfterYear'], self.params['BeforeYear'] = year, year
            self.params['AfterMonth'], self.params['BeforeMonth'] = month, month

            url = self.base_url + urlparse.urlencode(self.params)
            self.log(f'Begin crawling url {url}')
            yield Request(url, callback=self.parse, cb_kwargs={'month': month, 'year': year})

    # noinspection PyMethodOverriding
    def parse(self, response, month, year):
        titles = response.css('.issue-item__title a')
        urls = response.css('div.issue-item__detail a.issue-item__doi::text').getall()
        citations = response.css('span.citation span::text').getall()

        for title, url, citation in zip(titles, urls, citations):
            title_content = title.css('::text').get()
            title_link = title.css('::attr(href)').get()
            item = CcspiderItem(title=title_content, month=month, year=year, url=url, citation=citation)

            # visit detail pages of papers for other information
            yield response.follow(title_link, callback=self._parse_paper_detail, cb_kwargs={'item': item})

        # go to next page
        next_page = response.css('a.pagination__btn--next::attr(href)').get()
        if next_page is not None:
            yield scrapy.Request(next_page, callback=self.parse, cb_kwargs={'month': month, 'year': year})

    def _parse_paper_detail(self, response, item):
        self.log('begin crawling detail page information')

        item['abstract'] = _remove_html(response.css('div.abstractSection.abstractInFull p').get())

        # get author information
        item['authors'] = []
        for author_info in response.css('div.auth-info'):
            author_name = author_info.css('span.auth-name a::text').get().strip()
            author_affiliation = author_info.css('span.auth-institution::text').get().strip()
            item['authors'].append((author_name, author_affiliation))

        # get indexed term
        item['subjects'] = response.css('.article__index-terms ol.level-1 > li > div a::text').getall()
        item['subjects'].append('|||')  # a separator to separate level-1 terms and level-2 terms
        item['subjects'].extend(response.css('.article__index-terms ol.level-2 > li > div a::text').getall())

        return item
