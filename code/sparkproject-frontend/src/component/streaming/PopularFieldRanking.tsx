import React, { Component } from 'react';
import ChartRace from './ChartRace';
import echarts from '../../utilities/echarts';
import {Col, Row, InputNumber, Button, Space} from 'antd';
import { SearchOutlined, CaretRightOutlined } from '@ant-design/icons';
import {getColorDict} from '../../utilities/color';
import {URL, sendRequest} from "../../utilities/axios";

interface IState {
    dynamicRankingData: Array<any>,
    oneMonthData: Array<any>,
    title: string,
    index: number,
    year: number,
    selectedYear: number,
    initial: boolean,
    colorDict: {},
    searchButtonDisabled: boolean,
    playButtonDisabled: boolean,
    playingInterval: any,
    dataNumber: number,
    annualData: any
}

class PopularFieldRanking extends Component<any, IState>{
    constructor(props: any){
        super(props);
        this.state = {
            dynamicRankingData: [],
            oneMonthData: [],
            title: '',
            index: 0,
            year: 2016,
            selectedYear: 2016,
            initial: true,
            colorDict: {},
            searchButtonDisabled: false,
            playButtonDisabled: true,
            playingInterval: null,
            dataNumber: 0,
            annualData: []
        };
    }

    yearSelected = () => {
        if (this.state.year !== this.state.selectedYear || this.state.initial) {
            clearInterval(this.state.playingInterval);
            this.setState({selectedYear: this.state.year, searchButtonDisabled: true, playButtonDisabled: true, initial: false}, () => {
                sendRequest(URL.POPULAR_FIELD_RANKING, {year: this.state.selectedYear}, (originalData) => {
                    let nameSet = new Set();
                    originalData.fields.forEach((field) => { nameSet.add(field); });
                    const colorDict = getColorDict(nameSet);
                    this.setState({
                        dynamicRankingData: originalData.rankings,
                        index: 0,
                        colorDict: colorDict,
                        playButtonDisabled: false,
                        searchButtonDisabled: false,
                    });
                    this.setRankingData();
                });

                sendRequest(URL.POPULAR_ANNUAL_FIELD, {year: this.state.selectedYear}, (annualData) => {
                    this.setState({
                        annualData: annualData
                    });
                });
            });
        }
    };

    startPlay = () => {
        this.setState({index: 0, playButtonDisabled: true, searchButtonDisabled: true});
        const playingInterval = setInterval(() => {
            if (this.state.index >= this.state.dynamicRankingData.length) {
                clearInterval();
                this.setState({
                    playButtonDisabled: false,
                    searchButtonDisabled: false,
                    playingInterval: null
                });
            } else {
                this.loadDynamicRanking();
            }
        }, 500);
        this.setState({playingInterval: playingInterval});
    };

    setRankingData = () => {
        const oneMonthDataWithTitle = this.state.dynamicRankingData[this.state.index];
        const title = oneMonthDataWithTitle.date;
        const oneMonthData = oneMonthDataWithTitle.fields.map((fieldInfo) => {
            return {
                id: fieldInfo.name,
                title: fieldInfo.name,
                value: fieldInfo.paperNumber,
                color: this.state.colorDict[fieldInfo.name]
            }
        });
        this.setState({
            oneMonthData: oneMonthData,
            title: title
        });
    };

    loadDynamicRanking = () => {
        this.setRankingData();
        this.setState({
            index: this.state.index + 1
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
                data: this.state.annualData.map((e) => e.year),
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                data: this.state.annualData.map((e) => e.count),
                type: 'bar'
            }],
            label:{
                show: true,
                position: 'top',
                formatter: (param) => {
                    return this.state.annualData[param.dataIndex].field;
                }
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
                    <span>起始年份</span>
                    <InputNumber min={2010} max={2016} defaultValue={2016} onChange={(year) => {this.setState({year: Number(year)})}} />
                    <Button shape='circle' icon={<SearchOutlined/>} type='primary' onClick={this.yearSelected} disabled={this.state.searchButtonDisabled}/>
                </Space>
                <Row gutter={120}>
                    <Col span={13}>
                        <div style={{textAlign: 'center'}} >
                            <Space>
                                <span style={{fontSize: 20, fontWeight: 'bold', marginRight: 16}}>各领域论文数量 ({this.state.selectedYear}-2020)</span>
                                <Button shape='circle' icon={<CaretRightOutlined/>} onClick={this.startPlay}
                                        disabled={this.state.playButtonDisabled} />
                                <span>{this.state.title}</span>
                            </Space>
                        </div>
                        <ChartRace
                            data={this.state.oneMonthData}
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

export default PopularFieldRanking;