import React from 'react';
import {Table, Row, Col, Button, Space, InputNumber} from 'antd';
import PaperNumbers from './PaperNumbers';
import echarts from '../../utilities/echarts';
import RankingChart from './RankingChart';
import { SearchOutlined } from '@ant-design/icons';
import {URL, sendRequest} from "../../utilities/axios";

interface IState {
    columns: Array<any>,
    fieldData: Array<any>,
    paperNumberData: Array<any>,
    rankingData: Array<any>,
    title: string,
    visible1: boolean,
    visible2: boolean,
    year: number,
}

class PopularFields extends React.Component<any, IState> {
    constructor(props: any) {
        super(props);
        this.state = {
            columns: [], fieldData: [], paperNumberData: [], rankingData: [],
            title: '', visible1: false, visible2: false, year: 2016
        };
    }

    editPaperNumModal = (field) => {
        if (this.state.visible1) {
            this.setState({
                visible1: false
            });
            return
        }
        sendRequest(URL.PAPER_NUMBERS, {year: this.state.year, field: field}, (data) => {
            console.log(data);
            const paperNumberData = data.map((e) => {return {year: e.year, paperNumber: e.paperNumber}});
            this.setState({paperNumberData: paperNumberData, visible1: true})
        })
    };

    editRankingModal = (field, key) => {
        if (this.state.visible2) {
            this.setState({
                visible2: false
            });
            return
        }
        if (key === 'paper') {
            sendRequest(URL.POPULAR_PAPERS, {year: this.state.year, field: field}, (data) => {
                const rankingData = data.map((e) => {return {name: e.paper, number: e.citation}});
                this.setState({rankingData: rankingData, title: `${field}领域热门文章（基于引用数）`, visible2: true})
            });
        } else if (key === 'author') {
            sendRequest(URL.POPULAR_AUTHORS, {year: this.state.year, field: field}, (data) => {
                const rankingData = data.map((e) => {return {name: e.author, number: e.citation}});
                this.setState({rankingData: rankingData, title: `${field}领域热门作者（基于引用数）`, visible2: true})
            });
        }
    };

    loadTable() {
        sendRequest(URL.POPULAR_FIELDS, {year: this.state.year}, (fieldData) => {
            const columns = [
                { title: '所属领域', dataIndex: 'field', key: 'field' },
                { title: '文章数量', dataIndex: 'paperNumber', key: 'paperNumber' },
                { title: '作者数量', dataIndex: 'authorNumber', key: 'authorNumber' },
                { title: '信息查看', key: 'action', width: 240,
                    render: (text, record) => (
                        <Space>
                            <Button type='link' size='small' onClick={() => this.editPaperNumModal(record.field)}>详细热度</Button>
                            <Button type='link' size='small' onClick={() => this.editRankingModal(record.field, 'paper')}>论文引用</Button>
                            <Button type='link' size='small' onClick={() => this.editRankingModal(record.field, 'author')}>热门作者</Button>
                        </Space>
                    )
                }
            ];
            this.setState({
                columns: columns,
                fieldData: fieldData
            });
        });
    }

    loadChart() {
        var myChart = echarts.init(document.getElementById('pieChart'));
        myChart.setOption({
            title: {
                text: '各领域热度',
                subtext: '基于论文数量',
                left: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)'
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: this.state.fieldData.map((e) => {
                    return e.field
                }),
            },
            series: [
                {
                    name: '论文发表数',
                    type: 'pie',
                    radius: '50%',
                    center: ['60%', '50%'],
                    data: this.state.fieldData.map((e) => {
                        return {
                            name: e.field,
                            value: e.paperNumber
                        }
                    }),
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        });
        window.onresize = () => {
            myChart.resize();
        };
    }

    componentDidMount(): void {
        this.loadTable();
    }

    componentDidUpdate(): void {
        this.loadChart();
    }

    yearSelected = () => {
        this.loadTable();
        this.loadChart();
    };

    render() {
        return (
            <div>
                <Row gutter={80}>
                    <Col span={12}>
                        <Space style={{marginBottom: 16}}>
                            <span>起始年份</span>
                            <InputNumber min={2010} max={2016} defaultValue={this.state.year} onChange={(year) => {this.setState({year: Number(year)})}} />
                            <Button shape='circle' icon={<SearchOutlined/>} type='primary' onClick={this.yearSelected}/>
                        </Space>
                        <Table dataSource={this.state.fieldData} columns={this.state.columns}
                               pagination={false} bordered={true} size={'small'} scroll={{y: 600}}/>
                    </Col>
                    <Col span={12}>
                        <div id={'pieChart'} style={{width: '100%', height: '100%', marginTop: 64}} />
                    </Col>
                </Row>
                <PaperNumbers visible={this.state.visible1} width={600} onCancel={this.editPaperNumModal} paperNumberData={this.state.paperNumberData}/>
                <RankingChart
                    visible={this.state.visible2} width={600} onCancel={this.editRankingModal}
                    rankingData={this.state.rankingData} title={this.state.title}
                />
            </div>
        )
    }
}

export default PopularFields;