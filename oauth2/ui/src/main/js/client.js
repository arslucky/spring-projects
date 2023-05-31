'use strict'

import Cookies from 'js-cookie';

const rest = require('rest');
const mime = require('rest/interceptor/mime');
const template = require('rest/interceptor/template');
const errorCode = require('rest/interceptor/errorCode');
const defaultRequest = require('rest/interceptor/defaultRequest');

const client = new Promise(function(resolve) {
    
    let csrfToken = Cookies.get('XSRF-TOKEN');
    //console.log('csrfToken 1:' + csrfToken);
    let clientToken = rest
        .wrap(defaultRequest, { headers: { 'X-XSRF-TOKEN': csrfToken } })
        .wrap(mime)
        .wrap(errorCode);

    clientToken({ method: 'POST', path: '/token' }).then(
        response => {
            let token = "Bearer ".concat(response.entity);
            let csrfToken = Cookies.get('XSRF-TOKEN');
            //console.log('csrfToken 2:' + csrfToken);
            let clientApp = rest
                .wrap(defaultRequest, { headers: { 'X-XSRF-TOKEN': csrfToken, 'Authorization': token } })
                .wrap(mime)
                .wrap(template)
                .wrap(errorCode);

            resolve(clientApp);
        }
    ).catch(e => {
        console.log('Error:');
        console.log(e);
    });
});

export { client };