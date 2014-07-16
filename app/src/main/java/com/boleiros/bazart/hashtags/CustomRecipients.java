package com.boleiros.bazart.hashtags;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.MultiAutoCompleteTextView;

import com.android.ex.chips.RecipientEditTextView;

/**
 * Created by diego on 7/14/14.
 */
public class CustomRecipients extends RecipientEditTextView {

    public CustomRecipients(Context context, AttributeSet attrs) {
        super(context, attrs);
        RecipientsEditorTokenizer tokenizer = new RecipientsEditorTokenizer();
        setTokenizer(tokenizer);
    }




    private char mLastSeparator = ',';

    @Override
    public void onTextChanged(CharSequence s, int start,
                              int before, int after) {
        if (before == 0 && after == 1) {    // inserting a character
            char c = s.charAt(start);
            if (c == ',' || c == ';'|| c == ' ') {
                // Remember the delimiter the user typed to end this recipient. We'll
                // need it shortly in terminateToken().
                mLastSeparator = c;
            }
        }
    }
    private class RecipientsEditorTokenizer
            implements MultiAutoCompleteTextView.Tokenizer {
        @Override
        public int findTokenStart(CharSequence text, int cursor) {

            int i = cursor;
            char c;
            // If we're sitting at a delimiter, back up so we find the previous token
            if (i > 0 && ((c = text.charAt(i - 1)) == ',' || c == ';'|| c == ' ')) {
                --i;
            }
            // Now back up until the start or until we find the separator of the previous token
            while (i > 0 && (c = text.charAt(i - 1)) != ',' && c != ';' && c != ' ' ) {
                i--;
            }
            while (i < cursor) {
                i++;
            }
            return i;
        }

        @Override
        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();
            char c;
            while (i < len) {
                if ((c = text.charAt(i)) == ',' || c == ';'|| c == ' ') {
                    return i;
                } else {
                    i++;
                }
            }
            return len;
        }


        @Override
        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();
            while (i > 0) {
                i--;
            }
            char c;
            if (i > 0 && ((c = text.charAt(i - 1)) == ',' || c == ';'|| c == '!')) {
                return text;
            } else {
                // Use the same delimiter the user just typed.
                // This lets them have a mixture of commas and semicolons in their list.
                String separator = mLastSeparator + "";
                if (text instanceof Spanned) {
                    SpannableString sp = new SpannableString(text + separator);
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                            Object.class, sp, 0);
                    return sp;
                } else {
                    return text + separator;
                }
            }
        }
    };
}
