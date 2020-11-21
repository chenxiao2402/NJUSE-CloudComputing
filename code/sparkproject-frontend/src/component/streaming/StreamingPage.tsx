import React, {FC, lazy, Suspense} from 'react';
import {Route, Switch} from 'react-router-dom';
import {Radio} from 'antd';
import {RadioChangeEvent} from 'antd/lib/radio';
import routes from '../../utilities/routes';

const TopFieldOfYear = lazy( () => import('./PopFieldOfYear'));
const DynamicRanking = lazy(() => import('./DynamicRanking'));
const onChange = (e: RadioChangeEvent) => {
    const path = e.target.value;
    window.location.href = path;
};

const StreamingPage: FC = () => {
    return (
        <div>
            <Radio.Group defaultValue={window.location.pathname} buttonStyle='solid' onChange={onChange}
                         style={{marginBottom: 16, textAlign: 'center', width: '100%'}}>
                <Radio.Button value={routes.topFieldOfYear}>热门领域</Radio.Button>
                <Radio.Button value={routes.dynamicRanking}>领域排名</Radio.Button>
            </Radio.Group>

            <Suspense fallback={<div>Loading...</div>}>
                <Switch>
                    <Route path={routes.topFieldOfYear} component={TopFieldOfYear} />
                    <Route path={routes.dynamicRanking} component={DynamicRanking} />
                </Switch>
            </Suspense>
        </div>
    )
};

export default StreamingPage;