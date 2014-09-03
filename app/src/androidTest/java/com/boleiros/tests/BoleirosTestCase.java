package com.boleiros.tests;

import android.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.boleiros.bazart.feed.Feed;
import com.parse.ui.ParseLoginFragment;
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

    public void testLogin(){

        assertTrue(solo.searchText("Entre com o Facebook"));//Irá verificar se existe o texto &quot;Usuário&quot; na tela

        solo.clickOnView(solo.getView(com.parse.ui.R.id.facebook_login));

        assertTrue("Não apareceu o 'Loading...'", solo.waitForText("Loading..."));
        assertTrue("Não apareceu o 'Loading...'", solo.waitForText("Loading..."));

        //assertTrue(solo.waitForActivity("ParseLoginActivity"));
        //assertTrue(solo.waitForFragmentById(com.parse.ui.R.id.parse_login));
        //assertTrue(solo.waitForView(com.parse.ui.R.id.parse_login));

        assertTrue("Não apareceu a tela de Login", solo.waitForText("Facebook account"));


    }


}
