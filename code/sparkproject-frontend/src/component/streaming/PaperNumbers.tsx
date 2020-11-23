import React, {Component} from 'react';
import {Modal} from 'antd';
import echarts from '../../utilities/echarts';

const defaultProps = {
    visible: false,
    width: 1000,
    onCancel: undefined,
    paperNumberData: []
};

type Props = {} & Partial<typeof defaultProps>;


export default class PaperNumbers extends Component<Props, any> {

    componentDidUpdate(): void {
        try {
            const myChart = echarts.init(document.getElementById('paperNumberChart'));
            const startYear = this.props.paperNumberData[0].year;
            const endYear = this.props.paperNumberData[this.props.paperNumberData.length - 1].year;
            myChart.setOption({
                title: {
                    text: 'DeepLearning领域论文发表数',
                    subtext: `${startYear}-${endYear}年`,
                    left: 'center'
                },
                xAxis: {
                    type: 'category',
                    data: this.props.paperNumberData.map((e) => e.year)
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    data: this.props.paperNumberData.map((e) => e.paperNumber),
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