package com.touchlink.medespoir.MVP.views.ui.callbacks;

import android.view.View;

import com.touchlink.medespoir.MVP.models.network.Article;

public interface ArticleListener {


        void onClick(View view, int position, Article article);

}
