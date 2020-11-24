# Define here the models for your scraped items
#
# See documentation in:
# https://docs.scrapy.org/en/latest/topics/items.html

import scrapy
from scrapy import Item, Field


class CcspiderItem(Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    title = Field()
    authors = Field()  # { 'author-name' : 'affiliation' }
    month = Field()
    year = Field()
    subjects = Field()
    abstract = Field()
    citation = Field()
    url = Field()
