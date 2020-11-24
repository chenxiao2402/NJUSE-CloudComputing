import ROUTES from './routes';

const redirectURL = () => {
    const urls = [ROUTES.HOME, ROUTES.STREAMING];
    const targets = [ROUTES.POPULAR_FIELDS, ROUTES.POPULAR_FIELDS];
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