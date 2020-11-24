"""放一些可能需要公用的常用方法"""

_month_list = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October',
               'November', 'December']
_month_dict = {}
for i in range(12):
    _month_dict[_month_list[i]] = i + 1


def merge_text(dic: [str]) -> str:
    return ''.join(dic).strip()


def month_to_int(month: str) -> int:
    return _month_dict[month]


def deduplicate(_list: []) -> []:
    return list(set(_list))


class DateRange:

    def __init__(self, start_year, start_month, end_year, end_month):
        self.start_year = start_year
        self.start_month = max(1, min(12, start_month))
        self.end_year = end_year
        self.end_month = max(1, min(12, end_month))

    def __iter__(self):
        self.curr = (self.start_year, self.start_month)
        self.iter_number = 0  # recording number of iterations
        return self

    def __next__(self):
        if self.curr[0] > self.end_year or (self.curr[0] == self.end_year and self.curr[1] > self.end_month):
            raise StopIteration

        result = self.curr
        next_month = self.curr[1] + 1 if self.curr[1] < 12 else 1
        next_year = self.curr[0] + 1 if self.curr[1] == 12 else self.curr[0]
        self.curr = (next_year, next_month)

        return result
