/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.nover.yana.installWizard.ui;

import fr.nover.yana.R;
import fr.nover.yana.installWizard.Config;
import fr.nover.yana.installWizard.model.AbstractWizardModel;
import fr.nover.yana.installWizard.model.ModelCallbacks;
import fr.nover.yana.installWizard.model.Page;
import fr.nover.yana.installWizard.model.ReviewItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReviewFragment extends ListFragment implements ModelCallbacks {
    private Callbacks mCallbacks;
    private AbstractWizardModel mWizardModel;
    private List<ReviewItem> mCurrentReviewItems;

 	Intent Resultat = new Intent("Resultat");
    
    private ReviewAdapter mReviewAdapter;

    public ReviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReviewAdapter = new ReviewAdapter();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page, container, false);

        TextView titleView = (TextView) rootView.findViewById(android.R.id.title);
        titleView.setText(R.string.review);
        titleView.setTextColor(getResources().getColor(R.color.review_green));

        ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        setListAdapter(mReviewAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new ClassCastException("Activity must implement fragment's callbacks");
        }

        mCallbacks = (Callbacks) activity;

        mWizardModel = mCallbacks.onGetModel();
        mWizardModel.registerListener(this);
        onPageTreeChanged();
    }

    @Override
    public void onPageTreeChanged() {
        onPageDataChanged(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;

        mWizardModel.unregisterListener(this);
    }

    @Override
    public void onPageDataChanged(Page changedPage) {
        ArrayList<ReviewItem> reviewItems = new ArrayList<ReviewItem>();
        for (Page page : mWizardModel.getCurrentPageSequence()) {
            page.getReviewItems(reviewItems);
        }
        Collections.sort(reviewItems, new Comparator<ReviewItem>() {
            @Override
            public int compare(ReviewItem a, ReviewItem b) {
                return a.getWeight() > b.getWeight() ? +1 : a.getWeight() < b.getWeight() ? -1 : 0;
            }
        });
        mCurrentReviewItems = reviewItems;

        if (mReviewAdapter != null) {
            mReviewAdapter.notifyDataSetInvalidated();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallbacks.onEditScreenAfterReview(mCurrentReviewItems.get(position).getPageKey());
    }

    public interface Callbacks {
        AbstractWizardModel onGetModel();
        void onEditScreenAfterReview(String pageKey);
    }

    private class ReviewAdapter extends BaseAdapter {
        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public Object getItem(int position) {
            return mCurrentReviewItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mCurrentReviewItems.get(position).hashCode();
        }

        @Override
        public View getView(int position, View view, ViewGroup container) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View rootView = inflater.inflate(R.layout.list_item_review, container, false);

            ReviewItem reviewItem = mCurrentReviewItems.get(position);
            String value = reviewItem.getDisplayValue();
            if (TextUtils.isEmpty(value)) {
                value = "(None)";
            }
            String type = reviewItem.getTitle();
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(type);
            ((TextView) rootView.findViewById(android.R.id.text2)).setText(value);
            
            if(value.compareTo("(None)")==0){value="";}
            
            if(type.compareTo("Nom")==0){
				Config.Nom=value;}
            
			else if(type.compareTo("Pr�nom")==0){
				Config.Pr�nom=value;}
            
			else if(type.compareTo("Pseudonyme")==0){
				Config.Pseudonyme=value;}
            
			else if(type.compareTo("Vous �tes un(e)...")==0){
				Log.d("R�ussite !", value);
				if(value.compareTo("")==0){
					Config.Sexe=3;}
				else if(value.compareTo("Mademoiselle")==0){
					Config.Sexe=0;}
				else if(value.compareTo("Madame")==0){
					Config.Sexe=1;}
				else if(value.compareTo("Monsieur")==0){
					Config.Sexe=2;}}
            
			else if(type.compareTo("Lien interne")==0){
				Config.IPAdress=value;}
            
			else if(type.contains("local")){
				Log.d("SSID",value);
				Config.SSID=value;}
			            
			else if(type.compareTo("Lien externe")==0){
				Config.IPadress_ext=value;}
            
			else if(type.compareTo("Vous voulez activer ...")==0){
				
				if(value.contains("ShakeService")){
					Config.ShakeService=true;}
				else{Config.ShakeService=false;}
				
				if(value.contains("�v�nements")){
					Config.EventService=true;}
				else{Config.EventService=false;}
				
				if(value.contains("synth�se")){
					Config.TTS=true;}
				else{Config.TTS=false;}

				if(value.contains("commandes")){
					Config.Update_Com=true;}
				else{Config.Update_Com=false;}}
            
			else if(type.contains("Internet")){
				if(value.compareTo("Oui")==0){Config.externe=true;}
				else{Config.externe=false;}}
            
            return rootView;}

        @Override
        public int getCount() {
            return mCurrentReviewItems.size();
        }
    }
}
