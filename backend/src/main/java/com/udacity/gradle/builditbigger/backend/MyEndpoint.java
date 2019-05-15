package com.udacity.gradle.builditbigger.backend;

import com.example.jokelib.Joker;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

/** An endpoint class we are exposing */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.builditbigger.gradle.udacity.com",
                ownerName = "backend.builditbigger.gradle.udacity.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    /** A simple endpoint method that takes a name and says Hi back */
    /*@ApiMethod(name = "sayHi")
    public Joke sayHi(@Named("name") String name) {
        Joke response = new Joke();
        response.setData("Hi, " + name);

        return response;
    }*/

    @ApiMethod(name = "tellJoke")
    public Joke tellJoke() {
        String jokeText = new Joker().tellJoke();
        Joke response = new Joke();
        response.setData(jokeText);

        return response;
    }

}
