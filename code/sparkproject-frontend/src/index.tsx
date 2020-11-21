import React from 'react';
import ReactDOM from 'react-dom';
import './style/index.css';
import App from './component/App';
import {BrowserRouter} from 'react-router-dom';


// 关于router的解决
// https://stackoverflow.com/questions/41474134/nested-routes-with-react-router-v4-v5
ReactDOM.render(
    <BrowserRouter >
        <App/>
    </BrowserRouter>,
    document.getElementById('root')
);