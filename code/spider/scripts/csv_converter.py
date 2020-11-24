import pandas as pd

from scripts.process_subject import map_acm_to_arxiv


def process_subject(subjects):
    # find the index of the separator '|||' which
    # separates level-1 subjects and level-2 subjects
    level_2_index = subjects.rfind('|||')
    if level_2_index > 0:
        subjects = subjects[level_2_index + 4:]

    # process the subjects whose name contains a comma
    assert isinstance(subjects, str)
    split_subjects = subjects.split(',')
    final = []
    for i in range(len(split_subjects)):
        curr_subject = split_subjects[i]

        if curr_subject.startswith(' '):
            continue
        elif i == len(split_subjects) - 1:
            final.append(curr_subject)
            break

        next_subject = split_subjects[i + 1]
        if next_subject.startswith(' '):
            final.append(curr_subject + ', ' + next_subject)
        else:
            final.append(curr_subject)

    # do subjects mapping
    return map_acm_to_arxiv(final)


def process_authors(authors_str):
    res = [a[0] for a in eval(authors_str)]
    return list(set(res))  # deduplicate


def process_file(file_name):
    data = pd.read_csv(file_name, header=0).dropna()
    data = data[~data['subjects'].str.endswith('|||')]

    data['authors'] = data['authors'].map(process_authors)
    data['subjects'] = data['subjects'].map(process_subject)
    data['citation'] = data['citation'].map(lambda x: int(x.replace(',', '')) if isinstance(x, str) else int(x))
    data = data.drop(columns=['abstract', 'url'])
    data = data[data['subjects'].str.len() > 0]

    data.to_json(r'..\json\{}.json'.format(file_name[:-4]), orient='records')
    return data
