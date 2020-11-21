import React from 'react';
import {Table, Row, Col, Button, Space, InputNumber} from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import RelatedFieldsChart from './RelatedFieldsChart';
import AuthorConnectionChart from './AuthorConnectionChart';


interface IState {
    columns: Array<any>,
    dataSource: Array<any>,
    field: string,
    year: number,
    showRelatedFields: boolean,
    showAuthorConnection: boolean,
    author: String
}

class GraphXPage extends React.Component<any, IState> {
    constructor(props: any) {
        super(props);
        this.state = {
            columns: [], dataSource: [],
            field: '', year: 5,
            showRelatedFields: false,
            showAuthorConnection: false,
            author: ''
        };
    }

    loadTable() {
        const columns = [
            {
                title: '所属领域',
                dataIndex: 'field',
                key: 'field',
            },
            {
                title: '文章数量',
                dataIndex: 'paperNumber',
                key: 'paperNumber'
            },
            {
                title: '作者数量',
                dataIndex: 'authorNumber',
                key: 'authorNumber'
            },
            {
                title: '信息查看',
                key: 'action',
                render: (text, record) => (
                    <Space>
                        <Button type='link' size='small' onClick={() => this.loadChart(record.field, 'showRelatedFields')}>相关领域</Button>
                        <Button type='link' size='small' onClick={() => this.loadChart(record.field, 'showAuthorConnection')}>合作关系</Button>
                    </Space>
                )
            }
        ];
        const dataSource = [
            {
                field: 'Machine Learning',
                paperNumber: 2341,
                authorNumber: 1123
            },
            {
                field: 'Static Analysis',
                paperNumber: 2241,
                authorNumber: 982
            },
            {
                field: 'Computer Vision',
                paperNumber: 2159,
                authorNumber: 1010
            },
            {
                field: 'Security',
                paperNumber: 2021,
                authorNumber: 789
            },
            {
                field: 'Database',
                paperNumber: 1860,
                authorNumber: 1200
            },
            {
                field: 'Compiling',
                paperNumber: 1721,
                authorNumber: 945
            },
            {
                field: 'NLP',
                paperNumber: 1621,
                authorNumber: 887
            },
            {
                field: 'Speech Recognition',
                paperNumber: 1541,
                authorNumber: 680
            },
            {
                field: 'Network',
                paperNumber: 1200,
                authorNumber: 703
            },
            {
                field: 'OS',
                paperNumber: 1157,
                authorNumber: 923
            },
            {
                field: 'Graphics',
                paperNumber: 1241,
                authorNumber: 884
            },
            {
                field: 'HCI',
                paperNumber: 941,
                authorNumber: 645
            },
            {
                field: 'Algorithm',
                paperNumber: 885,
                authorNumber: 623
            },
            {
                field: 'Distributed System',
                paperNumber: 741,
                authorNumber: 523
            },
            {
                field: 'SE',
                paperNumber: 606,
                authorNumber: 555
            },
            {
                field: 'Testing',
                paperNumber: 500,
                authorNumber: 324
            },
            {
                field: 'Programming Language',
                paperNumber: 463,
                authorNumber: 334
            }
        ];

        this.setState({
            columns: columns,
            dataSource: dataSource
        });
    }

    loadChart(field, key) {
        this.setState({
            field: field,
            showRelatedFields: key === 'showRelatedFields',
            showAuthorConnection: key === 'showAuthorConnection'
        });
    }

    yearSelected = () => {
        this.loadTable();
    };

    componentDidMount(): void {
        this.loadTable();
    }

    render() {
        return (
            <div>
                <Space style={{marginBottom: 16}}>
                    <span>统计年份数</span>
                    <InputNumber min={5} max={50} defaultValue={5} onChange={(year) => {this.setState({year: Number(year)})}} />
                    <Button shape='circle' icon={<SearchOutlined/>} type='primary' onClick={this.yearSelected}/>
                </Space>
                <Row gutter={0}>
                    <Col span={14}>
                        <div style={{width: '100%', textAlign: 'center', marginBottom: 4}}>
                            <span style={{fontSize: 20, fontWeight: 'bolder'}}>领域交叉度排名</span>
                        </div>
                        <Table dataSource={this.state.dataSource} columns={this.state.columns}
                               pagination={false} bordered={true} size={'small'} scroll={{y: 600}}/>
                    </Col>
                    <Col span={10}>
                        <div style={{width: '100%', height: '100%', marginTop: 24}}>
                            <RelatedFieldsChart visible={this.state.showRelatedFields} field={this.state.field} year={this.state.year}/>
                            <AuthorConnectionChart visible={this.state.showAuthorConnection} field={this.state.field} year={this.state.year}/>
                        </div>
                    </Col>
                </Row>
            </div>
        )
    }
}

export default GraphXPage;