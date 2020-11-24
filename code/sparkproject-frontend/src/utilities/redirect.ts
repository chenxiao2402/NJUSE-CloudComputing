import ROUTES from './routes';

const redirectURL = () => {
    const urls = [ROUTES.HOME];
    const targets = [ROUTES.STREAMING];
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