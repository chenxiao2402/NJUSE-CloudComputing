import React, {Component, lazy, Suspense} from 'react';
import {Route, Switch} from 'react-router-dom';
import {Radio} from 'antd';
// import { LoadingOutlined, ReloadOutlined } from '@ant-design/icons';
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
    dataNumber: number,
    dataStableDuration: number,
    streamingData: Array<any>
}

class StreamingPage extends Component<any, IState> {

    constructor(props: any){
        super(props);
        this.state = {
            loadingStream: false,
            dataNumber: 0,
            dataStableDuration: 0,
            streamingData: []
        };
    }

    // loadStream = () => {
    //     if (!localStorage.getItem('dataLoaded')) {
    //     //     this.setState({
    //     //         loadingStream: true
    //     //     });
    //     //     setInterval(() => {
    //     //         if (localStorage.getItem('dataLoaded')) {
    //     //             clearInterval();
    //     //             this.forceUpdate();
    //     //             window.location.href = ROUTES.POPULAR_FIELDS;
    //     //         } else {
    //     //             sendRequest(URL.YEAR_PAPER_COUNT, {}, (data) => {
    //     //                 console.log(data);
    //     //                 const dataNumber = data.map(e => e.count).reduce((a, b) => a + b);
    //     //                 const dataStableDuration = this.state.dataStableDuration + (this.state.dataNumber === dataNumber ? 1 : 0);
    //     //                 this.setState({
    //     //                     dataNumber: dataNumber,
    //     //                     dataStableDuration: dataStableDuration,
    //     //                 });
    //     //                 if (dataStableDuration >= 5) {
    //     //                     localStorage.setItem('dataLoaded', 'true');
    //     //                 }
    //     //                 this.setBarChart(data);
    //     //
    //     //             })
    //     //         }
    //     //     }, 1000);
    //         sendRequest(URL.START_PAPER_COUNT, {}, () => {
    //             this.setState({
    //                 loadingStream: true
    //             });
    //             setInterval(() => {
    //                 if (localStorage.getItem('dataLoaded')) {
    //                     clearInterval();
    //                     this.forceUpdate();
    //                     window.location.href = ROUTES.POPULAR_FIELDS;
    //                 } else {
    //                     sendRequest(URL.YEAR_PAPER_COUNT, {}, (data) => {
    //                         const dataNumber = data.map(e => e.count).reduce((a, b) => a + b);
    //                         const dataStableDuration = this.state.dataStableDuration + (this.state.dataNumber === dataNumber ? 1 : 0);
    //                         this.setState({
    //                             dataNumber: dataNumber,
    //                             dataStableDuration: dataStableDuration,
    //                         });
    //                         if (dataStableDuration >= 5) {
    //                             localStorage.setItem('dataLoaded', 'true');
    //                         }
    //                         this.setBarChart(data);
    //                     })
    //                 }
    //             }, 1000);
    //         });
    //     } else {
    //         window.location.href = ROUTES.POPULAR_FIELDS;
    //         this.forceUpdate();
    //     }
    // };

    componentDidMount(): void {
        sendRequest(URL.YEAR_PAPER_COUNT, {}, (data) => {
            const dataNumber = data.map(e => e.count).reduce((a, b) => a + b);
            this.setState({
                dataNumber: dataNumber
            });
            this.setBarChart(data);
        });
    }

    setBarChart(data) {
        var myChart = echarts.init(document.getElementById('streamingChart'));
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
                    data: data.map(e => e.year),
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
                    name: '直接访问',
                    type: 'bar',
                    barWidth: '60%',
                    data: data.map(e => e.count)
                }
            ]
        });
    }

    render() {
        // alert(localStorage.getItem('dataLoaded'));
        return (
            // !localStorage.getItem('dataLoaded') ?
            //     <div>
            //         <div style={{textAlign: 'center'}}>
            //             <span style={{width: 180, display: 'inline-block', textAlign: 'left'}}>流读取论文数：{this.state.dataNumber}</span>
            //             {this.state.loadingStream ? <LoadingOutlined /> : <Button shape='circle' icon={<ReloadOutlined/>} onClick={this.loadStream} />}
            //         </div>
            //         <div id={'streamingChart'} style={{width: '100%', height: 700 }}/>
            //     </div> :
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
