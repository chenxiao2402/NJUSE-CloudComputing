import React, {Component} from 'react';
import echarts from '../../utilities/echarts';
import {URL, sendRequest} from "../../utilities/axios";

const defaultProps = {
    visible: false,
    field: 'Machine Learning',
    year: 2016
};

type Props = {} & Partial<typeof defaultProps>;


export default class RelatedFieldsChart extends Component<Props, any> {

    componentDidUpdate(): void {
        if (this.props.visible) {
            sendRequest(URL.RELATED_FIELDS, {year: this.props.year, field: this.props.field}, (data) => {
                const reversedData = data.reverse();
                const myChart = echarts.init(document.getElementById('relatedFieldsChart'));
                var option = {
                    title: {
                        text:  `${this.props.field}相关领域（${this.props.year}-2020）`,
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
                        data: reversedData.map((e) => {
                            const words = e.field.split('(')[0].split(' ');
                            let result = [];
                            let line = '';
                            for (let word of words) {
                                line += `${word} `;
                                if (line.length >= 20) {
                                    result.push(`${line}`);
                                    line = ''
                                }
                            }
                            if (line.length < 20) {
                                result.push(line)
                            }
                            return result.join('\n');
                        }),
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
                        data: reversedData.map((e) => e.paperNumber),
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
            });
        }
    }

    render() {
        return (
            this.props.visible ? <div id={'relatedFieldsChart'} style={{width: '100%', height: '100%'}}/> : null
        );
    }
};