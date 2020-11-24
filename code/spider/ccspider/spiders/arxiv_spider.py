"""专门爬arxiv数据的模块"""
import scrapy
import ccspider.utils as utils
from ccspider.items import CcspiderItem


class ArxivSpider(scrapy.Spider):
    """专门爬arxiv数据的爬虫"""
    name = 'arxiv'

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.db_table_name = 'arxiv_paper_info'
        self.allowed_domains = ['arxiv.org']
        self.start_urls = []
        base_url = 'https://arxiv.org/search/advanced?advanced=1&terms-0-operator=AND&terms-0-term=' \
                   '&terms-0-field=title&classification-computer_science=y&classification-physics_archives=all' \
                   '&classification-include_cross_list=include&date-filter_by=specific_year&date-year={year}' \
                   '&date-from_date=&date-to_date=&date-date_type=submitted_date&abstracts=show&size={size}' \
                   '&order=-announced_date_first&start={start}'
        year = 2019
        self.size = 200
        self.page = 1
        page_count = 256
        for page in range(50, page_count):
            self.start_urls.append(base_url.format(year=year, size=self.size, start=page * self.size))

    def parse(self, response: scrapy.http.response.Response, **kwargs):
        data_list = response.xpath('//*[@id="main-container"]/div[2]/ol/li')
        for data in data_list:
            item = CcspiderItem()
            item['title'] = data.xpath('.//p[1]/text()')[2].get().strip()
            item['authors'] = data.xpath('.//p[2]/a/text()').extract()
            date = utils.merge_text(data.xpath('.//p[4]/text()[last()]').get())
            date = date.split(' ')
            item['month'] = utils.month_to_int(date[0])
            item['year'] = int(date[1][:4])
            item['subjects'] = utils.deduplicate(data.xpath('.//div/div/span/@data-tooltip').extract())
            item['abstract'] = utils.merge_text(data.xpath('.//p[3]/span[3]/text()').get())
            item['citation'] = 0
            yield item
        print('已爬完{}页（共{}条）'.format(self.page, self.page * self.size))
        self.page += 1
