import React, {Component} from 'react';
import {Space, Select,  Button} from 'antd';
import { UserOutlined, SearchOutlined } from '@ant-design/icons';
import CollaboratorModal from './CollaboratorModal';
import {URL, sendRequest} from "../../utilities/axios";
import echarts from "../../utilities/echarts";
// import {COLORS} from "../../utilities/color";
const { Option } = Select;

const defaultProps = {
    visible: false,
    field: 'Machine Learning',
    year: 5,
    onClick: Function,
};

interface IState {
    author: string,
    sourceName: string,
    targetName: string,
    modalVisible: boolean,
    routeData: Array<any>
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
            routeData: []
        };
    }

    getRandomInt = (min: number, max: number) => {
        min = Math.ceil(min);
        max = Math.floor(max);
        return Math.floor(Math.random() * (max - min + 1)) + min;
    };

    names = [
        'Xin Wen', 'Tianyang Li', 'Zhizhong Han', 'Yu-Shen Liu', 'Yen-Chang Hsu', 'Yilin Shen',
        'Hongxia Jin', 'Zsolt Kira', 'Grigorios G. Chrysos', 'Stylianos Moschoglou', 'Giorgos Bouritsas',
        'Yannis Panagakis', 'Jiankang Deng', 'Stefanos Zafeiriou', 'Tian Han', 'Erik Nijkamp',
        'Linqi Zhou', 'Bo Pang', 'Song-Chun Zhu', 'Ying Nian Wu', 'Yang Liu', 'Xu Tang', 'Junyu Han',
        'Jingtuo Liu', 'Dinger Rui', 'Xiang Wu', 'Gordon Christie', 'Rodrigo Rene Rai Munoz Abujder',
        'Kevin Foster', 'Shea Hagstrom', 'Gregory D. Hager', 'Myron Z. Brown', 'Yuhang Cao', 'Kai Chen',
        'Chen Change Loy', 'Dahua Lin', 'Ying Chen', 'Chun-Guang Li', 'Chong You', 'Zhihong Chen',
        'Chao Chen', 'Zhaowei Cheng', 'Boyuan Jiang', 'Ke Fang', 'Xinyu Jin', 'Seongwook Yoon',
        'Sanghoon Sull', 'Seung Wook Kim', 'Yuhao Zhou', 'Jonah Philion', 'Antonio Torralba', 'Sanja Fidler',
        'Seung Wook Kim', 'Yuhao Zhou', 'Jonah Philion', 'Antonio Torralba', 'Sanja Fidler', 'Fengmao Lv',
        'Tao Liang', 'Xiang Chen', 'Guosheng Lin', 'Thomas Elsken', 'Benedikt Staffler', 'Jan Hendrik Metzen',
        'Jiankang Deng', 'Stefanos Zafeiriou', 'Tian Han', 'Erik Nijkamp', 'Linqi Zhou', 'Bo Pang',
        'Song-Chun Zhu', 'Ying Nian Wu', 'Yang Liu', 'Xu Tang', 'Junyu Han', 'Jingtuo Liu', 'Dinger Rui',
        'Xiang Wu', 'Gordon Christie', 'Rodrigo Rene Rai Munoz Abujder', 'Kevin Foster', 'Shea Hagstrom',
        'Gregory D. Hager', 'Myron Z. Brown', 'Yuhang Cao', 'Kai Chen', 'Chen Change Loy', 'Dahua Lin',
        'Ying Chen', 'Chun-Guang Li', 'Chong You', 'Zhihong Chen', 'Chao Chen', 'Zhaowei Cheng', 'Boyuan Jiang',
        'Ke Fang', 'Xinyu Jin', 'Seongwook Yoon', 'Sanghoon Sull', 'Seung Wook Kim', 'Yuhao Zhou',
        'Jonah Philion', 'Antonio Torralba', 'Sanja Fidler', 'Fengmao Lv', 'Tao Liang', 'Xiang Chen',
        'Guosheng Lin', 'Thomas Elsken', 'Benedikt Staffler', 'Jan Hendrik Metzen', 'Frank Hutter',
        'Chen-Lin Zhang', 'Yun-Hao Cao', 'Jianxin Wu', 'Zhengsu Chen', 'Jianwei Niu', 'Lingxi Xie', 'Xuefeng Liu',
        'Longhui Wei', 'Qi Tian', 'Shaoshuai Shi', 'Chaoxu Guo', 'Li Jiang', 'Zhe Wang', 'Jianping Shi',
        'Hongsheng Li', 'Jinjin Gu', 'Yujun Shen', 'Bolei Zhou', 'Aymen Mir', 'Thiemo Alldieck',
        'Gerard Pons-Moll', 'Junhwa Hur', 'Stefan Roth', 'Christopher Choy', 'Junha Lee', 'Ren√© Ranftl',
        'Jaesik Park', 'Vladlen Koltun', 'Ying-Cong Chen', 'Xiaogang Xu', 'Jiaya Jia', 'Paul Bergmann',
        'Michael Fauser', 'David Sattlegger', 'Carsten Steger', 'Ren Yang', 'Fabian Mentzer', 'Luc Van Gool',
        'Radu Timofte', 'Matthew J. Vowels', 'Necati Cihan Camg√∂z', 'Richard Bowden', 'Ildoo Kim',
        'Woonhyuk Baek', 'Sungwoong Kim', 'Xin Wen', 'Tianyang Li', 'Zhizhong Han', 'Yu-Shen Liu',
        'Yen-Chang Hsu', 'Yilin Shen', 'Hongxia Jin', 'Zsolt Kira', 'Grigorios G. Chrysos',
        'Stylianos Moschoglou', 'Giorgos Bouritsas', 'Yannis Panagakis', 'Jiankang Deng', 'Stefanos Zafeiriou',
        'Tian Han', 'Erik Nijkamp', 'Linqi Zhou', 'Bo Pang', 'Song-Chun Zhu', 'Ying Nian Wu', 'Yang Liu',
        'Xu Tang', 'Junyu Han', 'Jingtuo Liu', 'Dinger Rui', 'Xiang Wu', 'Gordon Christie',
        'Rodrigo Rene Rai Munoz Abujder', 'Kevin Foster', 'Shea Hagstrom', 'Gregory D. Hager', 'Myron Z. Brown',
    ];

    fakeData = () => {
        let graph = {
            links: [],
            nodes: []
        };

        let set = new Set();
        let categories = [];
        let sentinel = 0;
        let category = 0;
        while (sentinel < this.names.length) {
            category ++;
            categories.push(`${category}`);
            const groupSize = this.getRandomInt(10, 30);
            const maxId = Math.min(sentinel + groupSize, this.names.length - 1);
            while (sentinel <= maxId) {
                const collaborators = this.getRandomInt(3, 10);
                graph.nodes.push({
                    category: category.toString(),
                    id: sentinel.toString(),
                    name: this.names[sentinel],
                    value: collaborators,
                    symbolSize: collaborators * 2
                });
                sentinel ++;
            }
            for (let i = 0; i < 4 * groupSize; i++) {
                const a = this.getRandomInt(maxId - groupSize, maxId);
                const b = this.getRandomInt(maxId - groupSize, maxId);
                const str = `${a}#${b}`;
                if (!set.has(str) && a < b) {
                    set.add(str);
                    graph.links.push({
                        id: str,
                        target: a.toString(),
                        source: b.toString(),
                    });
                }
            }
        }
        for (let i = 0; i < 0.5 * this.names.length; i++) {
            const a = this.getRandomInt(0, this.names.length - 1);
            const b = this.getRandomInt(0, this.names.length - 1);
            const str = `${a}#${b}`;
            if (!set.has(str) && a < b) {
                set.add(str);
                graph.links.push({
                    id: str,
                    target: a.toString(),
                    source: b.toString(),
                });
            }
        }

        return {graph, categories};
    };

    componentDidUpdate(): void {
        if (!localStorage.getItem('dataLoaded') || localStorage.getItem('field') !== this.props.field) {
            sendRequest(URL.AUTHOR_CONNECTIONS, {year: this.props.year, field: this.props.field}, (data) => {
                const myChart = echarts.init(document.getElementById('authorConnectionChart'));

                myChart.setOption( {
                    // color: COLORS,
                    title: {
                        text: `${this.props.field}领域合作关系图（2016-2020）`,
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
                    legend: [{
                        // selectedMode: 'single',
                        data: data.categories,
                        orient: 'vertical',
                        left: 'right',
                        top: 'center',
                        // formatter: ' '
                    }],
                    series : [
                        {
                            name: '合作者数量',
                            type: 'graph',
                            layout: 'force',
                            data: data.nodes,
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
                myChart.on('click', (param) => {
                    const name = param.name;
                    if (name.indexOf('#') < 0) {
                        this.setState({
                            author: param.name
                        })
                    }
                });
                window.onresize = () => {
                    myChart.resize();
                };
                localStorage.setItem('dataLoaded', 'true');
                localStorage.setItem('field', this.props.field);
            })
        }
        if (!this.props.visible) {
            localStorage.removeItem('dataLoaded');
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