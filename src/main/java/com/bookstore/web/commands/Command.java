package com.bookstore.web.commands;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * A single front-controller action. Returns the logical view name to forward to
 * (resolved against {@code /WEB-INF/views/{view}.jsp}), or {@code null} when the
 * handler has already written/redirected the response.
 */
public interface Command {

    String execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException;
}
