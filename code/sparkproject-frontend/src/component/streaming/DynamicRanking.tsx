import React, { Component } from 'react';
import ChartRace from './ChartRace';
import echarts from '../../utilities/echarts';
import {Col, Row, InputNumber, Button, Space} from 'antd';
import { SearchOutlined, CaretRightOutlined, UploadOutlined, LoadingOutlined} from '@ant-design/icons';
import {getColorDict} from '../../utilities/color';
// import {url, loadData} from "../../utilities/axios";

interface IState {
    originalData: Array<any>,
    rankData: Array<any>,
    title: string,
    index: number,
    year: number,
    colorDict: {},
    playing: boolean,
    playingInterval: any,
    loadingStream: boolean
    loadingStreamInterval: any,
    dataNumber: number
}

class DynamicRanking extends Component<any, IState>{
    constructor(props: any){
        super(props);
        this.state = {
            originalData: [],
            rankData: [],
            title: '',
            index: 0,
            year: 5,
            colorDict: {},
            playing: false,
            playingInterval: null,
            loadingStream: false,
            loadingStreamInterval: null,
            dataNumber: 0
        };
    }

    getRandomInt = (min: number, max: number) => {
        min = Math.ceil(min);
        max = Math.floor(max);
        return Math.floor(Math.random() * (max - min + 1)) + min;
    };

    fakeData = () => {
        // get data of year
        const year1 = this.state.year;
        const months = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'];
        const fields = ['ML', 'DL', 'System', 'Database', 'Security', 'Static Analysis', 'Compiling', 'Network', 'HCI', 'CV'];
        const numberDict = {};
        // const colorDict = getColorDict(new Set(fields));
        let data = [];
        for (let year = 2020 - year1 + 1; year <= 2020; year ++) {
            for (let month of months) {
                for (let field of fields) {
                    if (numberDict[field] === undefined) {
                        numberDict[field] = this.getRandomInt(10, 90);
                    } else {
                        numberDict[field] += this.getRandomInt(10, 90);
                    }
                }
                var items = Object.keys(numberDict).map((key) => {
                    return {
                        name: key,
                        paperNumber: numberDict[key]
                    };
                });

                items.sort((first, second) => {
                    return second.paperNumber - first.paperNumber;
                });

                const mostEight = items.slice(0, 10);

                data.push({
                    date: year.toString() + '-' + month,
                    fields: mostEight
                })
            }
        }
        return data;
    };

    yearSelected = () => {
        clearInterval(this.state.playingInterval);
        clearInterval(this.state.loadingStreamInterval);

        this.setState({playing: false, loadingStream: false});

        const originalData = this.fakeData();
        let nameSet = new Set();
        originalData.forEach((originalData) => {
            originalData.fields.forEach((fieldInfo) => {
                nameSet.add(fieldInfo.name)
            })
        });
        const colorDict = getColorDict(nameSet);
        this.setState({
            originalData: originalData,
            index: 0,
            colorDict: colorDict
        }, () => {
            this.setRankingData();
        });
    };

    startPlay = () => {
        this.setState({index: 0, playing: true});
        const playingInterval = setInterval(() => {
            if (this.state.index >= this.state.originalData.length) {
                clearInterval();
                this.setState({
                    playing: false,
                    playingInterval: null
                });
            } else {
                this.loadDynamicRanking();
            }
        }, 500);
        this.setState({playingInterval: playingInterval});
    };

    setRankingData = () => {
        const originalData = this.state.originalData[this.state.index];
        const title = originalData.date;
        const rankData = originalData.fields.map((fieldInfo) => {
            return {
                id: fieldInfo.name,
                title: fieldInfo.name,
                value: fieldInfo.paperNumber,
                color: this.state.colorDict[fieldInfo.name]
            }
        });
        this.setState({
            rankData: rankData,
            title: title
        });
    };

    loadDynamicRanking = () => {
        this.setRankingData();
        this.setState({
            index: this.state.index + 1
        });
    };

    loadStream = () => {
        const loadingStreamInterval = setInterval(() => {
            // loadData(url.authorConnections, {}, (data) => {
            //
            // })
            this.setState({
                dataNumber: this.state.dataNumber + 500
            })
        }, 500);
        this.setState({
            loadingStream: true,
            loadingStreamInterval: loadingStreamInterval
        });
    };

    componentDidMount(): void {
        this.yearSelected();
    }

    componentDidUpdate(): void {
        var myChart = echarts.init(document.getElementById('barChart'));
        myChart.setOption({
            title: {
                text: '年度热门领域',
                subtext: '基于论文数量',
                left: 'center'
            },
            xAxis: {
                type: 'category',
                data: ['2016', '2017', '2018', '2019', '2020']
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                data: [2312, 2213, 1921, 2523, 2678],
                type: 'bar'
            }],

            label:{
                show: true,
                position: 'top',
                formatter: (param) => {
                    if(param.dataIndex === 0){
                        return 'Machine Learning';
                    }
                    if(param.dataIndex === 1){
                        return 'Computer Vision';
                    }
                    if(param.dataIndex === 2){
                        return 'Security';
                    }
                    if(param.dataIndex === 3){
                        return 'HCI';
                    }
                    if(param.dataIndex === 4){
                        return 'Network';
                    }
                },
            }
        });
        window.onresize = () => {
            myChart.resize();
        };
    }

    render(){
        return(
            <div>
                <Space style={{marginLeft: 16, marginTop: 16, marginBottom: 32}}>
                    <span>统计年份数</span>
                    <InputNumber min={5} max={50} defaultValue={5} onChange={(year) => {this.setState({year: Number(year)})}} />
                    <Button shape='circle' icon={<SearchOutlined/>} type='primary' onClick={this.yearSelected}/>
                </Space>
                <Row gutter={120}>
                    <Col span={13}>
                        <div style={{textAlign: 'center'}} >
                            <Space>
                                <span style={{fontSize: 20, fontWeight: 'bold', marginRight: 16}}>各领域论文数量 ({2020 - this.state.year + 1}-2020)</span>
                                {this.state.loadingStream ? <LoadingOutlined /> : <Button shape='circle' icon={<UploadOutlined/>} onClick={this.loadStream} />}
                                <span style={{width: 180, display: 'inline-block', textAlign: 'left'}}>流读取论文数：{this.state.dataNumber}</span>
                                <Button shape='circle' icon={<CaretRightOutlined/>} onClick={this.startPlay}
                                        disabled={this.state.playing} />
                                <span>{this.state.title}</span>
                            </Space>
                        </div>
                        <ChartRace
                            data={this.state.rankData}
                            backgroundColor='#fff'
                            width={window.innerWidth * (13 / 24)}
                            padding={8}
                            itemHeight={40}
                            gap={12}
                            titleStyle={{ font: 'Bold 20px Arial', color: '#000' }}
                            valueStyle={{ font: 'normal 16px Arial', color: 'rgba(215,0,255, 0.42)' }}
                        />
                    </Col>
                    <Col span={11}>
                        <div id={'barChart'} style={{width: '100%', height: window.innerHeight * 0.65}}/>
                    </Col>
                </Row>
            </div>
        );
    }
}

export default DynamicRanking;