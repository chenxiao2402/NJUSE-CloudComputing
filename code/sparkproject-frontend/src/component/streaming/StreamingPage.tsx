import React, {Component, lazy, Suspense} from 'react';
import {Route, Switch} from 'react-router-dom';
import {Button, Radio} from 'antd';
import { LoadingOutlined, UploadOutlined } from '@ant-design/icons';
import {RadioChangeEvent} from 'antd/lib/radio';
import ROUTES from '../../utilities/routes';
import {URL, sendRequest} from "../../utilities/axios";
import echarts from "../../utilities/echarts";

const PopularFields = lazy( () => import('./PopularFields'));
const DynamicRanking = lazy(() => import('./PopularFieldRanking'));
const onChange = (e: RadioChangeEvent) => {
    const path = e.target.value;
    window.location.href = path;
};

interface IState {
    loadingStream: boolean,
    showLoadingPage: boolean,
    dataNumber: number,
    dataStableDuration: number,
    streamingData: Array<any>
}

class StreamingPage extends Component<any, IState> {

    constructor(props: any){
        super(props);
        this.state = {
            loadingStream: false,
            showLoadingPage: !Boolean(localStorage.getItem('streamLoaded')),
            dataNumber: 0,
            dataStableDuration: 0,
            streamingData: []
        };
    }

    loadStream = () => {
        sendRequest(URL.START_PAPER_COUNT, {}, () => {
            this.setState({
                loadingStream: true
            });
            setInterval(() => {
                if (localStorage.getItem('streamLoaded')) {
                    clearInterval()
                } else {
                    sendRequest(URL.YEAR_PAPER_COUNT, {}, (data) => {
                        const dataStableDuration = this.state.dataStableDuration + (this.state.dataNumber === data.dataNumber ? 1 : 0);
                        if (dataStableDuration >= 3) {
                            localStorage.setItem('streamLoaded', 'true');
                        }
                        this.setState({
                            streamingData: data,
                            showLoadingPage: Boolean(localStorage.getItem('streamLoaded'))
                        });
                        this.setBarChart();
                    })
                }
            }, 1000);
        });
    };

    setBarChart = () => {
        if (this.state.showLoadingPage) {
            var myChart = echarts.init(document.getElementById('streamingChart'));
            alert();
            myChart.setOption({
                color: ['#3398DB'],
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: [
                    {
                        type: 'category',
                        data: this.state.streamingData.map(e => e.year),
                        axisTick: {
                            alignWithLabel: true
                        }
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: [
                    {
                        name: '已读取数据量',
                        type: 'bar',
                        barWidth: '60%',
                        data: this.state.streamingData.map(e => e.count)
                    }
                ]
            });
        }
    };

    componentDidMount(): void {
        sendRequest(URL.YEAR_PAPER_COUNT, {}, (data) => {
            this.setState({
                streamingData: data
            });
            this.setBarChart();
        })
    }

    componentDidUpdate(): void {

    }

    render() {
        return (
            this.state.showLoadingPage ?
                <div>
                    <div style={{textAlign: 'center'}}>
                        <span style={{width: 180, display: 'inline-block', textAlign: 'left'}}>流读取论文数：{this.state.dataNumber}</span>
                        {this.state.loadingStream ? <LoadingOutlined /> : <Button shape='circle' icon={<UploadOutlined/>} onClick={this.loadStream} />}
                    </div>
                    <div id={'streamingChart'} style={{width: '0.6ww', height: 700, marginTop: 64}} />
                </div> :
                <div>
                    <Radio.Group defaultValue={window.location.pathname} buttonStyle='solid' onChange={onChange}
                                 style={{marginBottom: 16, textAlign: 'center', width: '100%'}}>
                        <Radio.Button value={ROUTES.POPULAR_FIELDS}>热门领域</Radio.Button>
                        <Radio.Button value={ROUTES.DYNAMIC_RANKING}>领域排名</Radio.Button>
                    </Radio.Group>

                    <Suspense fallback={<div>Loading...</div>}>
                        <Switch>
                            <Route path={ROUTES.POPULAR_FIELDS} component={PopularFields} />
                            <Route path={ROUTES.DYNAMIC_RANKING} component={DynamicRanking} />
                        </Switch>
                    </Suspense>
                </div>
        )
    }
}

export default StreamingPage;