import React, {Component} from 'react';
import {Modal} from 'antd';
import echarts from '../../utilities/echarts';

const defaultProps = {
    visible: false,
    width: 1000,
    onCancel: undefined
};

type Props = {} & Partial<typeof defaultProps>;


export default class PaperNumChart extends Component<Props, any> {

    componentDidUpdate(): void {
        try {
            const myChart = echarts.init(document.getElementById('paperNumberChart'));
            myChart.setOption({
                title: {
                    text: 'DeepLearning领域论文发表数',
                    subtext: '2016-2020年',
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
                    data: [820, 932, 901, 1290, 1330],
                    type: 'line'
                }],
                tooltip: {
                    trigger: 'axis',
                    formatter: '{b}年: {c}篇'
                },
            });
        } catch (e) {
            console.log(e);
        }
    }

    render() {
        return (
            <Modal
                title={null}
                centered
                footer={null}
                visible={this.props.visible}
                width={this.props.width}
                onCancel={this.props.onCancel}
            >
                <div id={'paperNumberChart'} style={{width: this.props.width, height: this.props.width * 0.6}}/>
            </Modal>
        );
    }
};