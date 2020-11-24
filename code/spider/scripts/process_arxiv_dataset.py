import pandas as pd
from dateutil import parser

from scripts.process_subject import parse_arxiv_code


def process_authors_parsed(authors_parsed):
    # authors_parsed consists of lists of authors' name.
    # example:
    # authors_parsed = [['last_name', 'first_name', 'middle_name'], ...]
    result = []
    for name in authors_parsed:
        result.append(f'{name[1]} {name[0]}')
    return result


def process_subjects(subjects):
    result = []
    for s in subjects.split(' '):
        if s.startswith('cs.'):
            result.append(parse_arxiv_code(s[3:]))
    if len(result) == 0:
        print('Shit!')
    return result


def process_update_date(update_date):
    parsed_time = parser.parse(update_date)
    return parsed_time.year, parsed_time.month


def process_arxiv(file):
    data = pd.read_json(file, orient='records')
    data.dropna()

    if 'categories' in data.columns:
        data = data.rename(columns={'categories': 'subjects'})

    data['authors'] = data['authors_parsed'].map(process_authors_parsed)
    data['subjects'] = data['subjects'].map(process_subjects)
    data[['year', 'month']] = pd.DataFrame(data['update_date'].map(process_update_date).tolist(), index=data.index)

    output = data[['authors', 'title', 'subjects', 'year', 'month', 'citation']]
    output.to_json(r'..\json\arxiv_final.json', orient='records')
    return output
