import pandas as pd

from scripts.csv_converter import process_file
from scripts.process_arxiv_dataset import process_arxiv

file_list = [r'..\acm_{}.csv'.format(i) for i in range(2005, 2021)]
processed_data = []
for file in file_list:
    processed_data.append(process_file(file))

processed_data.append(process_arxiv(r'..\arxiv.json'))

# deduplicate
data = pd.concat(processed_data, ignore_index=True)
data = data.sort_values(by=['citation'], ascending=False)
data = data.drop_duplicates(['title'], keep='first')
data.to_json(r'../json/final_data.json', orient='records')
