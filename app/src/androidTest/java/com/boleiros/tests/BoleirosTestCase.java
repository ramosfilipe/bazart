package com.boleiros.tests;


import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;


import com.boleiros.bazart.R;
import com.boleiros.bazart.feed.Feed;
import com.robotium.solo.*;

/**
 * Created by walte on 02/09/2014.
 */
public class BoleirosTestCase  extends ActivityInstrumentationTestCase2<Feed>{
    private Solo solo;

    public BoleirosTestCase(){
        super(Feed.class);

    }

    @Override
    protected void setUp() throws Exception {

        super.setUp();
        solo = new Solo(getInstrumentation(),getActivity());

    }



    public void testAInit() throws Exception {

        if(solo.searchText("Entre com o Facebook")){
            assertTrue(solo.searchText("Entre com o Facebook"));
            solo.clickOnView(solo.getView(com.parse.ui.R.id.facebook_login));

            assertTrue("Não apareceu o 'Loading...'", solo.waitForText("Loading..."));
            assertTrue("Não apareceu o 'Loading...'", solo.waitForText("Loading..."));

            assertTrue(solo.waitForView(R.id.pager));
            assertTrue(solo.waitForView(R.id.textViewActionBar));
            assertTrue(solo.waitForText("Mais recentes", 1, 10000));

        }else {

            assertTrue(solo.waitForView(R.id.pager));
            assertTrue(solo.waitForView(R.id.textViewActionBar));


            assertTrue(solo.waitForText("Mais recentes", 1, 10000));
        }






        //
    }


    public void testBotaoAGPS(){
        assertTrue(solo.waitForView(R.id.gpsButton));
        solo.clickOnView(solo.getView(R.id.gpsButton));
        assertTrue(solo.waitForView(R.id.textViewActionBar));
        assertTrue(solo.waitForText("Mais próximos",1,10000));
    }


    public void testBotaoCamera(){
        assertTrue(solo.waitForView(R.id.cameraButton));
        solo.clickOnView(solo.getView(R.id.cameraButton));
        assertTrue(solo.waitForActivity("CameraActivity",10000));
        solo.goBackToActivity("Feed");
        assertTrue(solo.waitForActivity("Feed",10000));
    }


    public void testBotaoLike(){
        assertTrue(solo.waitForView(R.id.recomendacaoImageButton));
        solo.clickOnView(solo.getView(R.id.recomendacaoImageButton));
        assertTrue(solo.waitForView(R.id.textViewActionBar));
        assertTrue(solo.waitForText("Mais recomendados",1,10000));
    }

    public void testBotaoProfile(){
        assertTrue(solo.waitForView(R.id.profileImageButton));
        solo.clickOnView(solo.getView(R.id.profileImageButton));
        assertTrue(solo.waitForActivity("ProfileActivity", 10000));
        solo.goBackToActivity("Feed");
        assertTrue(solo.waitForActivity("Feed",10000));

    }


}
