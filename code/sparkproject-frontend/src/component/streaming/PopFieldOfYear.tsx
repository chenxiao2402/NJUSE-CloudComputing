import React from 'react';
import {Table, Row, Col, Button, Space, InputNumber} from 'antd';
import PaperNumChart from './PaperNumChart';
import echarts from '../../utilities/echarts';
import RankingChart from './RankingChart';
import { SearchOutlined } from '@ant-design/icons';

interface IState {
    columns: Array<any>,
    dataSource: Array<any>,
    rankingData: Array<any>,
    title: string,
    visible1: boolean,
    visible2: boolean,
    year: number,
}

class PopFieldOfYear extends React.Component<any, IState> {
    constructor(props: any) {
        super(props);
        this.state = {
            columns: [], dataSource: [],
            rankingData: [], title: '',
            visible1: false, visible2: false,
            year: 5
        };
        // this.editInfoModal = this.editInfoModal.bind(this);
    }

    editPaperNumModal = () => {
        this.setState({
            visible1: !this.state.visible1
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
            const data = [
                {
                    name: 'Perception of Spatial Relationships in Impossible Spaces',
                    number: 342
                },
                {
                    name: 'Interactive grammar development with WCDG',
                    number: 292
                },
                {
                    name: 'Extreme learning machine-based model for Solubility estimation of hydrocarbon gases in electrolyte solutions',
                    number: 242
                },
                {
                    name: 'Depth Extraction from Video Using Non-parametric Sampling',
                    number: 212
                },
                {
                    name: 'In-pipe Robotic System for Pipe-joint Rehabilitation in Fresh Water Pipes',
                    number: 192
                },
                {
                    name: 'Ultrasonic identification technique in recycling of lithium ion batteries',
                    number: 162
                },
                {
                    name: 'A Dynamic and Cooperative Tracking System for Crowdfunding',
                    number: 142
                },
                {
                    name: 'Embedding of FRPN in CNN architecture',
                    number: 82
                },
                {
                    name: 'Translating multispectral imagery to nighttime imagery via conditional generative adversarial networks',
                    number: 37
                }
            ];
            this.setState({rankingData: data, title: `${field}领域热门文章（基于引用数）`, visible2: true})
        } else if (key === 'author') {
            const data = [
                {
                    name: 'Narjes Nabipour',
                    number: 142
                },
                {
                    name: 'Amir Mosavi',
                    number: 121
                },
                {
                    name: 'Alireza Baghban',
                    number: 100
                },
                {
                    name: 'Shahaboddin Shamshirband',
                    number: 94
                },
                {
                    name: 'Imre Felde',
                    number: 82
                },
                {
                    name: 'Luis A. Mateos',
                    number: 76
                },
                {
                    name: 'Markus Vincze',
                    number: 65
                },
                {
                    name: 'Jianfei Cui',
                    number: 54
                },
                {
                    name: 'Dianbo Liu',
                    number: 37
                }
            ];
            this.setState({rankingData: data, title: `${field}领域热门作者（基于引用数）`, visible2: true})
        }
    };

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
                width: 240,
                render: (text, record) => (
                    <Space>
                        <Button type='link' size='small' onClick={this.editPaperNumModal}>详细热度</Button>
                        <Button type='link' size='small' onClick={() => this.editRankingModal(record.field, 'paper')}>论文引用</Button>
                        <Button type='link' size='small' onClick={() => this.editRankingModal(record.field, 'author')}>热门作者</Button>
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
                data: ['Machine Learning', 'Static Analysis', 'Computer Vision', 'Security', 'Database', 'Compiling', 'NLP', 'Speech Recognition', 'Network', 'OS', 'Graphics', 'HCI', 'Algorithm', 'Distributed System', 'SE', 'Testing', 'Programming Language']
            },
            series: [
                {
                    name: '论文发表数',
                    type: 'pie',
                    radius: '50%',
                    center: ['60%', '50%'],
                    data: [
                        {
                            name: 'Machine Learning',
                            value: 2341
                        },
                        {
                            name: 'Static Analysis',
                            value: 2241
                        },
                        {
                            name: 'Computer Vision',
                            value: 2159
                        },
                        {
                            name: 'Security',
                            value: 2021
                        },
                        {
                            name: 'Database',
                            value: 1860
                        },
                        {
                            name: 'Compiling',
                            value: 1721
                        },
                        {
                            name: 'NLP',
                            value: 1621
                        },
                        {
                            name: 'Speech Recognition',
                            value: 1541
                        },
                        {
                            name: 'Network',
                            value: 1200
                        },
                        {
                            name: 'OS',
                            value: 1157
                        },
                        {
                            name: 'Graphics',
                            value: 1241
                        },
                        {
                            name: 'HCI',
                            value: 941
                        },
                        {
                            name: 'Algorithm',
                            value: 885
                        },
                        {
                            name: 'Distributed System',
                            value: 741
                        },
                        {
                            name: 'SE',
                            value: 606
                        },
                        {
                            name: 'Testing',
                            value: 500
                        },
                        {
                            name: 'Programming Language',
                            value: 463
                        }
                    ],
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
                            <span>统计年份数</span>
                            <InputNumber min={5} max={50} defaultValue={5} onChange={(year) => {this.setState({year: Number(year)})}} />
                            <Button shape='circle' icon={<SearchOutlined/>} type='primary' onClick={this.yearSelected}/>
                        </Space>
                        <Table dataSource={this.state.dataSource} columns={this.state.columns}
                               pagination={false} bordered={true} size={'small'} scroll={{y: 600}}/>
                    </Col>
                    <Col span={12}>
                        <div id={'pieChart'} style={{width: '100%', height: '100%', marginTop: 64}} />
                    </Col>
                </Row>
                <PaperNumChart visible={this.state.visible1} width={600} onCancel={this.editPaperNumModal}/>
                <RankingChart
                    visible={this.state.visible2} width={600} onCancel={this.editRankingModal}
                    data={this.state.rankingData} title={this.state.title}
                />
            </div>
        )
    }
}

export default PopFieldOfYear;