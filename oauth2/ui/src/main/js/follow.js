function follow(api, settings, relArray) {

    const root = api.then(client => {
        return client({
            method: 'GET',
            path: settings.rootPath + settings.dataRestBasePath
        })
    });

    return relArray.reduce((root, arrayItem) => {
        return traverseNext(root, arrayItem);
    }, root)

    function traverseNext(root, arrayItem) {        
        return root.then(response => {
            let request = { method: 'GET' };

            if (typeof arrayItem === 'string') {
                request.path = response.entity._links[arrayItem].href;
            } else {
                request.path = response.entity._links[arrayItem.rel].href;
                request.params = arrayItem.params;
            }
            request.path = setGatewayPath(request.path, settings);
            return api.then(client => {
                return client(request);
            })
        })
    }
}

function setGatewayPath(path, settings) {
    path = path
        //.replace(/:[\d]{1,6}/, ':' + settings.appPort + settings.rootPath)
        .replace(/\/\/[\w-]+:[\d]{1,6}/, '//' + settings.appHost + ':' + settings.appPort + settings.rootPath)
        //.replace(/\{\?[\w,]+\}/, '')
        ;
    return path;
}

export {follow, setGatewayPath};
