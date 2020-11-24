import React from 'react';
import {Table, Row, Col, Button, Space, InputNumber} from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import RelatedFieldsChart from './RelatedFieldsChart';
import AuthorConnectionChart from './AuthorConnectionChart';
import {sendRequest, URL} from "../../utilities/axios";


interface IState {
    columns: Array<any>,
    intersectionData: Array<any>,
    field: string,
    year: number,
    selectedYear: number,
    searchButtonDisabled: boolean,
    showRelatedFields: boolean,
    showAuthorConnection: boolean,
    author: String
}

class GraphXPage extends React.Component<any, IState> {
    constructor(props: any) {
        super(props);
        this.state = {
            columns: [], intersectionData: [],
            field: '', year: 2016, selectedYear: 2016,
            searchButtonDisabled: false,
            showRelatedFields: false,
            showAuthorConnection: false,
            author: ''
        };
    }

    loadTable() {
        this.setState({selectedYear: this.state.year, searchButtonDisabled: true}, () => {
            sendRequest(URL.INTERSECTION_OF_FIELDS, {year: this.state.selectedYear}, (intersectionData) => {
                const columns = [
                    { title: '所属领域', dataIndex: 'field', key: 'field' },
                    { title: '文章数量', dataIndex: 'paperNumber', key: 'paperNumber' },
                    { title: '作者数量', dataIndex: 'authorNumber', key: 'authorNumber'},
                    { title: '信息查看', key: 'action',
                        render: (text, record) => (
                            <Space>
                                <Button type='link' size='small' onClick={() => this.loadChart(record.field, 'showRelatedFields')}>相关领域</Button>
                                <Button type='link' size='small' onClick={() => this.loadChart(record.field, 'showAuthorConnection')}>合作关系</Button>
                            </Space>
                        )
                    }
                ];
                this.setState({
                    columns: columns,
                    intersectionData: intersectionData,
                    searchButtonDisabled: false
                });
            });
        });
    }

    loadChart(field, key) {
        this.setState({
            field: field,
            showRelatedFields: key === 'showRelatedFields',
            showAuthorConnection: key === 'showAuthorConnection'
        });
    }

    yearSelected = () => {
        this.loadTable();
    };

    componentDidMount(): void {
        this.loadTable();
    }

    render() {
        return (
            <div>
                <Space style={{marginBottom: 16}}>
                    <span>起始年份</span>
                    <InputNumber min={2010} max={2016} defaultValue={2016} onChange={(year) => {this.setState({year: Number(year)})}} />
                    <Button shape='circle' icon={<SearchOutlined/>} type='primary' onClick={this.yearSelected} disabled={this.state.searchButtonDisabled}/>
                </Space>
                <Row gutter={0}>
                    <Col span={14}>
                        <div style={{width: '100%', textAlign: 'center', marginBottom: 4}}>
                            <span style={{fontSize: 20, fontWeight: 'bolder'}}>领域交叉度排名</span>
                        </div>
                        <Table dataSource={this.state.intersectionData} columns={this.state.columns}
                               pagination={false} bordered={true} size={'small'} scroll={{y: 600}}/>
                    </Col>
                    <Col span={10}>
                        <div style={{width: '100%', height: '100%', marginTop: 24}}>
                            <RelatedFieldsChart visible={this.state.showRelatedFields} field={this.state.field} year={this.state.selectedYear}/>
                            <AuthorConnectionChart visible={this.state.showAuthorConnection} field={this.state.field} year={this.state.selectedYear}/>
                        </div>
                    </Col>
                </Row>
            </div>
        )
    }
}

export default GraphXPage;