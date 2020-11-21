import React, {Component} from 'react';
import echarts from '../../utilities/echarts';

const defaultProps = {
    visible: false,
    field: 'Machine Learning',
    year: 5
};

type Props = {} & Partial<typeof defaultProps>;


export default class RelatedFieldsChart extends Component<Props, any> {

    fakeData = () => {
        return [
            {name: 'Software Engineering', paperNum: 968},
            {name: 'Security', paperNum: 830},
            {name: 'Database', paperNum: 772},
            {name: 'Network', paperNum: 720},
            {name: 'Static Analysis', paperNum: 683},
            {name: 'Deep Learning', paperNum: 582},
            {name: 'HCI', paperNum: 480},
            {name: 'Graphics', paperNum: 399},
            {name: 'Parallel Computing', paperNum: 274},
            {name: 'Compiling', paperNum: 198}
        ].reverse();
    };

    componentDidUpdate(): void {
        try {
            const myChart = echarts.init(document.getElementById('relatedFieldsChart'));
            const data = this.fakeData();
            var option = {
                title: {
                    text:  `${this.props.field}相关领域（2016-2020）`,
                    left: 'center'
                },
                grid: {
                    left: 16,
                    top: 48,
                    right: 32,
                    bottom: 32,
                    containLabel: true
                },
                xAxis: {
                    name: '跨领域论文数',
                    nameLocation: 'center',
                    type: 'value',
                    splitLine: {show: false},
                    axisLine: {show: false},
                    axisTick: {show: false},
                    axisLabel: {show: false},
                    nameTextStyle: {
                        fontSize: 16,
                        color: '#666',
                        fontWeight: 'Bolder'
                    }
                },
                yAxis: {
                    type: 'category',
                    data: data.map((e) => e.name),
                    axisTick: {show: false},
                    axisLine: {show: false},
                    axisLabel: {
                        margin: 10,
                        width: 100,
                        textStyle: {
                            color: '#333',
                            fontSize: 16
                        }
                    }},
                series: {
                    data: data.map((e) => e.paperNum),
                    type: 'bar',
                    showBackground: true,
                    backgroundStyle: {
                        color: 'rgba(220, 220, 220, 0.8)'
                    },
                    label: {
                        normal: {
                            show: true,
                            position: 'right',
                            textStyle: {
                                fontSize: 16,
                                fontWeight: 'Bolder'
                            }
                        }
                    },
                    symbol: 'rect',
                    itemStyle: {
                        normal: {
                            color: '#007eb1',
                        }
                    },
                }
            };
            myChart.setOption(option);
            window.onresize = () => {
                myChart.resize();
            };
        } catch (e) {
            console.log(e);
        }
    }

    render() {
        return (
            this.props.visible ? <div id={'relatedFieldsChart'} style={{width: '100%', height: '100%'}}/> : null
        );
    }
};