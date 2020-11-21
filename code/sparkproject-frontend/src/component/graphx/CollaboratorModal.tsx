import React, {Component} from 'react';
import {Modal, Steps, } from 'antd';
const {Step} = Steps;

const defaultProps = {
    visible: false,
    sourceName: '',
    targetName: '',
    width: 600,
    onCancel: undefined,
    data: []
};

type Props = {} & Partial<typeof defaultProps>;

export default class CollaboratorModal extends Component<Props, any> {

    render() {
        return (
            <Modal
                title={`${this.props.sourceName}和${this.props.targetName}的合作关系（2016-2020）`}
                centered
                footer={null}
                visible={this.props.visible}
                width={this.props.width}
                onCancel={this.props.onCancel}
            >
                <div style={{width: this.props.width, height: this.props.width * 0.5}}>
                    { this.props.data.map((route) => (
                        <Steps progressDot current={route.length} size={'small'} style={{width: this.props.width * 0.9, marginTop: 32}}>
                            {route.map((name) => <Step title={name} />)}
                        </Steps>
                    ))}
                </div>
            </Modal>
        )
    }
};