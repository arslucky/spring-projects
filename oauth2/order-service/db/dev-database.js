db = db.getSiblingDB('test');

/*db.dropUser('userTest');*/

db.createUser(
    {
      user: 'userTest',
      pwd: 'password',
      roles: [ { role: 'readWrite', db: 'test' } ]
    }
);

db = db.getSiblingDB('dev');

/*db.dropUser('userDev');*/

db.createUser(
    {
      user: 'userDev',
      pwd: 'password',
      roles: [ { role: 'readWrite', db: 'dev' } ]
    }
);

/*db.order.drop();
db.invoice.drop();
*/

var createdDt = new Date();

db.order.insertMany(
    [
      {
        _id: ObjectId("6438d220f21178489b7da7aa"),
        accountNumber: 'a12345',
        orderStatus: 'PENDING',
        createdAt: createdDt,
        lastModified: createdDt, 
        _class: 'org.demo.ars.domain.order.Order'
      },
      {
        _id: ObjectId("6438d221f21178489b7da7ac"),
        accountNumber: '12345',
        orderStatus: 'PENDING',
        createdAt: createdDt,
        lastModified: createdDt, 
        _class: 'org.demo.ars.domain.order.Order'
      }
    ]
);

db.invoice.insertOne(
    [
      {
        _id: ObjectId("6438d220f21178489b7da7ab"),
        customerId: 'c12345',
        orders: [ { accountNumber: 'a12345', orderStatus: 'PENDING' } ],
        ordersRef: [ DBRef("order", ObjectId("6438d220f21178489b7da7aa")) ],
        invoiceStatus: 'CREATED',
        createdAt: createdDt,
        lastModified: createdDt, 
        _class: 'org.demo.ars.domain.invoice.Invoice'
      }
    ]
);