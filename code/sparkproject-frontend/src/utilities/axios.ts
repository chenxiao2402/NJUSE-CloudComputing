import Axios from 'axios';
import qs from 'qs';

const axios = Axios.create({
    baseURL: "http://localhost:3333", // 这里是本地express启动的服务地址
    timeout: 5000 // request timeout
});
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';
axios.interceptors.request.use((config) => {
    if (config.method === 'post') {
        config.data = qs.stringify(config.data);
    }
    return config;
});


const url = {
    popularFields: 'PopularFields',
    paperNumbers: 'PaperNumbers',
    popularPapers: 'PopularPapers',
    popularAuthors: 'PopularAuthors',
    popularFieldRanking: 'PopularFieldRanking',
    popularAnnualField: 'PopularAnnualField',
    intersectionOfFields: 'IntersectionOfFields',
    relatedFields: 'RelatedFields',
    authorConnections: 'AuthorConnection',
    collaborators: 'Collaborators'
};

const loadData = (url, params: any, callback: Function) => {
    axios.get(`/${url}`, params)
        .then((response) => {
            callback(response.data);
        })
        .catch((error) => {
            console.log(error);
        });
};

export {url, loadData};