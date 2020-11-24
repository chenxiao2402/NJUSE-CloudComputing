# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://docs.scrapy.org/en/latest/topics/item-pipeline.html

from twisted.enterprise import adbapi
import pymysql


class MysqlPipeline:
    """
    把爬到的数据异步插入MySQL。要求spider拥有str类型的实例成员'db_table_name'，用以指定数据表名。
    """
    insert_sql = """
                 insert into {db_table_name}(title, authors, `month`, `year`, subjects, abstract, citation) 
                 VALUES (%s,%s,%s,%s,%s,%s,%s)
                 """

    def __init__(self, db_pool):
        self.db_pool = db_pool

    @classmethod
    def from_settings(cls, settings):  # 函数名固定，会被scrapy调用，直接可用settings的值
        """
        数据库建立连接
        :param settings: 配置参数
        :return: 实例化参数
        """
        adb_params = dict(
            host=settings['MYSQL_HOST'],
            db=settings['MYSQL_DBNAME'],
            user=settings['MYSQL_USER'],
            password=settings['MYSQL_PASSWORD'],
            cursorclass=pymysql.cursors.DictCursor  # 指定cursor类型
        )
        # 连接数据池ConnectionPool，使用pymysql连接
        db_pool = adbapi.ConnectionPool('pymysql', **adb_params)
        # 返回实例化参数
        return cls(db_pool)

    def open_spider(self, spider):
        """通过spider.db_table_name设置数据表名"""
        db_table_name = spider.db_table_name or 'paper_info'
        self.insert_sql = MysqlPipeline.insert_sql.format(db_table_name=db_table_name)

    def process_item(self, item, spider):
        """
        使用twisted将MySQL插入变成异步执行。通过连接池执行具体的sql操作，返回一个对象
        """
        query = self.db_pool.runInteraction(self.do_insert, item)  # 指定操作方法和操作数据
        # 添加异常处理
        query.addCallback(self.handle_error)  # 处理异常

    def do_insert(self, cursor, item):
        """对数据库进行插入操作，并不需要commit，twisted会自动commit"""
        cursor.execute(self.insert_sql, (item['title'], ','.join(item['authors']), item['month'], item['year'],
                                         ','.join(item['subjects']), item['abstract'], item['citation']))

    @staticmethod
    def handle_error(failure):
        """打印错误信息"""
        if failure:
            print('Error: ' + failure)
