import React, {Component} from 'react';
import {Space, Select,  Button} from 'antd';
import { UserOutlined, SearchOutlined } from '@ant-design/icons';
import CollaboratorModal from './CollaboratorModal';
import {URL, sendRequest} from "../../utilities/axios";
import echarts from "../../utilities/echarts";
import {COLORS} from "../../utilities/color";
const { Option } = Select;

const defaultProps = {
    visible: false,
    field: '',
    year: 2016,
    onClick: Function,
};

interface IState {
    author: string,
    sourceName: string,
    targetName: string,
    modalVisible: boolean,
    routeData: Array<any>,
    names: Array<any>
}

type Props = {} & Partial<typeof defaultProps>;

export default class RelatedFieldsChart extends Component<Props, IState> {

    constructor(props: any) {
        super(props);
        this.state = {
            author: '',
            sourceName: '',
            targetName: '',
            modalVisible: false,
            routeData: [],
            names: []
        };
    }

    names = [];

    componentDidUpdate(): void {
        if (!localStorage.getItem('dataLoaded3') || localStorage.getItem('field') !== this.props.field) {
            sendRequest(URL.AUTHOR_CONNECTIONS, {year: this.props.year, field: this.props.field}, (data) => {
                const names = new Set<String>();
                data.nodes.forEach((e) => { names.add(e.name)} );
                this.names = Array.from(names);

                const myChart = echarts.init(document.getElementById('authorConnectionChart'));
                myChart.setOption( {
                    color: COLORS,
                    title: {
                        text: `${this.props.field}领域合作关系图（${this.props.year}-2020）`,
                        top: 'bottom',
                        left: 'center'
                    },
                    grid: {
                        left: 0,
                        top: 0,
                        right: 0,
                        bottom: 0,
                        containLabel: true
                    },
                    tooltip: {},
                    // legend: [{
                    //     // selectedMode: 'single',
                    //     data: data.categories,
                    //     orient: 'vertical',
                    //     left: 'right',
                    //     top: 'center',
                    //     // formatter: ' '
                    // }],
                    series : [
                        {
                            name: '合作者数量',
                            type: 'graph',
                            layout: 'force',
                            data: data.nodes.map((e) => {return {
                                category: e.category,
                                id: e.id,
                                name: e.name,
                                value: e.collaborators === null ? 0 : e.collaborators
                                // symbolSize: e.collaborators * 3
                            }}),
                            links: data.links,
                            categories: data.categories.map((a) => {return {name: a}}),
                            roam: true,
                            focusNodeAdjacency: true,
                            itemStyle: {
                                borderColor: '#fff',
                                borderWidth: 1,
                                shadowBlur: 4,
                                shadowColor: 'rgba(0, 0, 0, 0.3)'
                            },
                            label: {
                                position: 'right',
                                formatter: '{b}'
                            },
                            lineStyle: {
                                // color: 'source',
                                curveness: 0.1
                            },
                            emphasis: {
                                lineStyle: {
                                    width: 10
                                }
                            }
                        }
                    ]
                });
                localStorage.setItem('dataLoaded3', 'true');
                localStorage.setItem('field', this.props.field);
                window.onresize = () => {
                    myChart.resize();
                };
            })
        }
        if (!this.props.visible) {
            localStorage.removeItem('dataLoaded3');
            localStorage.removeItem('field');
        }
    }

    editAuthorModal = () => {
        if (this.state.sourceName && this.state.targetName) {
            const data = [
                ['Yujun Shen'],
                ['Ying-Cong Chen', 'Aymen Mir'],
                ['Paul Bergmann', 'Christopher Choy'],
                ['Xiaogang Xu', 'Aymen Mir', 'Thiemo Alldieck'],
                ['Junha Lee', 'Vladlen Koltun', 'Jaesik Park'],
            ].map((e) => {
                return [this.state.sourceName].concat(e).concat([this.state.targetName]);
            });
            this.setState({
                routeData: data,
                modalVisible: !this.state.modalVisible
            })
        }
    };

    render() {
        const element =
            <div style={{width: '100%', height: '100%', textAlign: 'center'}}>
                <div style={{display: 'inline-block'}}>
                <Space style={{marginBottom: 16}}>
                    <Space style={{marginBottom: 16}} size={'large'}>
                        <span style={{fontSize: 18, fontWeight: 'bolder'}}>合作关系探索</span>
                        <Select suffixIcon={<UserOutlined/>} style={{ width: 150 }} onChange={(v) => {this.setState({sourceName: String(v)})}}>
                            {this.names.map((name) => {return <Option value={name}>{name}</Option>})}
                        </Select>
                        <Select suffixIcon={<UserOutlined/>} style={{ width: 150 }} onChange={(v) => {this.setState({targetName: String(v)})}}>
                            {this.names.map((name) => {return <Option value={name}>{name}</Option>})}
                        </Select>
                        <Button shape='circle' icon={<SearchOutlined/>} onClick={this.editAuthorModal}/>
                    </Space>
                </Space>
                </div>
                <div id={'authorConnectionChart'} style={{width: '100%', height: 'calc(100% - 48px)'}}/>
                <CollaboratorModal
                visible={this.state.modalVisible} width={800} onCancel={this.editAuthorModal}
                sourceName={this.state.sourceName} targetName={this.state.targetName} data={this.state.routeData}
                />
            </div>;
        return (
            this.props.visible ? element : null
        );
    }
};