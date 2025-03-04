
db.createUser({
    user: 'gooinprochatdbuser',
    pwd: 'gooinprochatdbuser',
    roles: [
        {
            role: 'readWrite',
            db: 'gooinprochatdb',
        },
    ],
});
