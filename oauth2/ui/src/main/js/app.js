'use strict';

import React from 'react';
import ReactDOM from 'react-dom';
import { createRoot } from 'react-dom/client';
import { client } from './client';
import { follow, setGatewayPath } from './follow';

const settings = {
    appHost: process.env.GTW_HOST,
    appPort: process.env.GTW_PORT,
    rootPath: '/customer',
    dataRestBasePath: '/api'
};

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = { customers: [], attributes: [], links: {}, pageSize: 2 };
        this.onCreate = this.onCreate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
        this.updatePageSize = this.updatePageSize.bind(this);
    }

    loadFromServer(pageSize) {
        follow(client, settings, [{ rel: 'customers', params: { size: pageSize } }])
            .then(response => {
                this.customers = response.entity._embedded.customers;
                this.link = setGatewayPath(response.entity._links.self.href, settings);
                this.links = response.entity._links;
                return response;
            })
            .then(response => {
                //console.log(response);
                client
                    .then(app => {
                        app({
                            method: 'GET',
                            path: setGatewayPath(response.entity._links.profile.href, settings),
                            headers: { 'Accept': 'application/schema+json' }
                        }).then(schema => {
                            this.setState({
                                customers: this.customers,
                                links: this.links,
                                attributes: schema.entity.properties
                            })
                        })
                    })
            })
    }

    onCreate(newEntity, link) {
        client
            .then(app => {
                return app({
                    method: 'POST',
                    path: link,
                    entity: newEntity,
                    headers: { 'Content-Type': 'application/json' }
                })
            })
            .then(response => {
                this.loadFromServer(this.state.pageSize);
            });
    }

    onDelete(entity) {
        client
            .then(app => {
                return app({
                    method: 'DELETE',
                    path: setGatewayPath(entity._links.self.href, settings)
                })
            }).then(response => {
                this.loadFromServer(this.state.pageSize);
            })
    }

    onNavigate(navUri) {
        client
            .then(app => {
                return app({
                    method: 'GET',
                    path: navUri
                })
            })
            .then(response => {
                this.setState({
                    customers: response.entity._embedded.customers,
                    links: response.entity._links,
                    attributes: this.state.attributes
                })

            })
    }

    updatePageSize(pageSize) {
        if (pageSize !== this.state.pageSize) {
            this.loadFromServer(pageSize);
        }
    }

    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
    }

    render() {
        return (
            <div>
                <CreateDialog attributes={this.state.attributes} onCreate={this.onCreate} link={this.link} />
                <CustomerList
                    customers={this.state.customers}
                    onDelete={this.onDelete}
                    onNavigate={this.onNavigate}
                    navLinks={this.state.links}
                    updatePageSize={this.updatePageSize} />
            </div>
        );
    }

}

class CustomerList extends React.Component {

    constructor(props) {
        super(props);
        this.handleNavigate = this.handleNavigate.bind(this);
        this.handleInput = this.handleInput.bind(this);
    }

    handleNavigate(e, navLink) {
        e.preventDefault();
        this.props.onNavigate(setGatewayPath(navLink, settings));
    }

    handleInput(e) {
        e.preventDefault();
        const pageSize = e.target.value.trim();
        if (/^[0-9]+$/.test(pageSize)) {
            this.props.updatePageSize(pageSize);
        } else {
            e.target.value =
                pageSize.substring(0, pageSize.length - 1);
        }
    }

    render() {
        const navLinks = [];

        if ("first" in this.props.navLinks) {
            navLinks.push(<button key="first" onClick={e => this.handleNavigate(e, this.props.navLinks["first"].href)}>&lt;&lt;</button>)
        }
        if ("prev" in this.props.navLinks) {
            navLinks.push(<button key="prev" onClick={e => this.handleNavigate(e, this.props.navLinks["prev"].href)}>&lt;</button>)
        }
        if ("next" in this.props.navLinks) {
            navLinks.push(<button key="next" onClick={e => this.handleNavigate(e, this.props.navLinks["next"].href)}>&gt;</button>)
        }
        if ("last" in this.props.navLinks) {
            navLinks.push(<button key="last" onClick={e => this.handleNavigate(e, this.props.navLinks["last"].href)}>&gt;&gt;</button>)
        }

        const customers = this.props.customers.map(customer => <Customer key={customer._links.self.href} customer={customer} onDelete={this.props.onDelete} />)
        return (
            <div>
                Page size:&nbsp;<input ref="pageSize" defaultValue={this.props.pageSize} onInput={this.handleInput} />
                <br /><br />
                <table>
                    <thead>
                        <tr>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Email</th>
                            <th></th>
                        </tr>
                    </thead>
                    {customers}
                </table>
                <div>
                    {navLinks}
                </div>
            </div>
        )
    }
}


class Customer extends React.Component {

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
    }

    handleDelete() {
        this.props.onDelete(this.props.customer);
    }

    render() {
        return (
            <tbody>
                <tr>
                    <td>{this.props.customer.firstName}</td>
                    <td>{this.props.customer.lastName}</td>
                    <td>{this.props.customer.email}</td>
                    <td><button onClick={this.handleDelete}>Delete</button></td>
                </tr>
            </tbody>
        )
    }
}

class CreateDialog extends React.Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        const newEntity = {};

        for (let attr of Object.keys(this.props.attributes)) {
            if (skipAttribute(attr) || skipAttribute(this.props.attributes[attr])) {
                continue;
            }
            let node = ReactDOM.findDOMNode(this.refs[attr]);
            newEntity[attr] = node.value.trim();
            node.value = '';
        }
        this.props.onCreate(newEntity, this.props.link);
        // Navigate away from the dialog to hide it.
        window.location = "#";
    }

    render() {
        const inputs = new Array();
        for (let [key, value] of Object.entries(this.props.attributes)) {
            if (skipAttribute(key) || skipAttribute(value)) {
                continue;
            }
            inputs.push(
                <p key={key}>
                    <input type="text" placeholder={value.title} ref={key} className="field" />
                </p>
            )
        }

        return (
            <div>
                <a href="#createCustomer">Create</a>

                <div id="createCustomer" className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>
                        <h2>Create new customer</h2>
                        <form>
                            {inputs}
                            <button onClick={this.handleSubmit}>Create</button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }
}

function skipAttribute(attribute) {
    if ((attribute.hasOwnProperty("format") && attribute.format == "uri")
        || attribute == "createdAt"
        || attribute == "lastModified") {
        return true;
    }
    return false;
}

const root = createRoot(document.getElementById('react'));
root.render(<App />);