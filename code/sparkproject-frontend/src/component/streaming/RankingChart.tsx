import React, {Component} from 'react';
import {Modal, Progress} from 'antd';

const defaultProps = {
    title: '',
    visible: false,
    width: 1000,
    onCancel: undefined,
    data: []
};

type Props = {} & Partial<typeof defaultProps>;


export default class RankingChart extends Component<Props, any> {

    draw(){
        try {
            const maxNumber = this.props.data[0].number;
            return(
                <div>
                    {this.props.data.map((item) => {
                        return (
                            <div>
                                <div style={{width: '100%'}}>
                                    <span style={{maxWidth: 450, color: '#1890ff'}}>{item.name}</span>
                                    <span style={{float: 'right', textAlign: 'center'}}>{item.number}</span>
                                </div>
                                <Progress strokeColor={{from: '#108ee9', to: '#87d068'}} percent={item.number / maxNumber * 100}
                                          style={{marginBottom: 16}} showInfo={false}/>
                            </div>
                        )
                    })}
                </div>
            );
        } catch (e) {
            return <div/>
        }
    }

    render() {
        return (
            <Modal
                title={this.props.title}
                centered
                footer={null}
                visible={this.props.visible}
                width={this.props.width}
                onCancel={this.props.onCancel}
            >
                {this.draw()}
            </Modal>
        )
    }
};