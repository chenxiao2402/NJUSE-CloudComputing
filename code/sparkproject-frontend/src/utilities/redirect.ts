import routes from './routes';

const redirectURL = () => {
    const urls = [routes.home, routes.streaming];
    const targets = [routes.topFieldOfYear, routes.topFieldOfYear];
    const index = urls.indexOf(window.location.pathname);
    if (index >= 0) {
        window.location.href = targets[index];
    }
};

const getSubject = () => {
    let path = window.location.pathname;
    path = path.replace(/.*\/Streaming.*/, '/Streaming');
    path = path.replace(/.*\/GraphX.*/, '/GraphX');
    return path;
};

export {redirectURL, getSubject};