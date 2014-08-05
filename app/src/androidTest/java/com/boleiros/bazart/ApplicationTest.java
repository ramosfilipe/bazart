package com.boleiros.bazart;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    Application mApplication;

    public ApplicationTest() {
        super(Application.class);
    }

    protected void setUp() throws Exception {
        createApplication();
        mApplication = getApplication();
    }
}