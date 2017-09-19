# Clockin

This application was generated using JHipster 3.10.0, you can find documentation and help at [https://jhipster.github.io/documentation-archive/v3.10.0](https://jhipster.github.io/documentation-archive/v3.10.0).

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:
1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools (like
[Bower][] and [BrowserSync][]). You will only need to run this command when dependencies change in package.json.

    npm install

We use [Gulp][] as our build system. Install the Gulp command-line tool globally with:

    npm install -g gulp-cli

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    gulp

Bower is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in `bower.json`. You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

## Building for production

Configure your clientId and clientSecret in `src/main/resources/config/application.yml`.
To see how create it, go to: https://developers.google.com/+/web/signin/server-side-flow#step_1_create_a_client_id_and_client_secret

Configure your database connection in `src/main/resources/config/application-prod.yml`

To optimize the clockin application for production, run:

    ./mvnw -Pprod clean package

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    nohup ./target/clockin-0.0.1-SNAPSHOT.war --spring.profiles.active=prod &

Then navigate to [http://localhost](http://localhost) in your browser.

Refer to [Using JHipster in production][] for more details.

## Testing

To launch your application's tests, run:

    ./mvnw clean test

### Client tests

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in `src/test/javascript/` and can be run with:

    gulp test

For more information, refer to the [Running tests page][].

[JHipster Homepage and latest documentation]: https://jhipster.github.io
[JHipster 3.10.0 archive]: https://jhipster.github.io/documentation-archive/v3.10.0

[Using JHipster in development]: https://jhipster.github.io/documentation-archive/v3.10.0/development/
[Using JHipster in production]: https://jhipster.github.io/documentation-archive/v3.10.0/production/
[Running tests page]: https://jhipster.github.io/documentation-archive/v3.10.0/running-tests/


[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Gulp]: http://gulpjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
