package com.mantis.MantisNotesIterationOne.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mantis.MantisNotesIterationOne.Adapters.ArticleAdapter;
import com.mantis.MantisNotesIterationOne.R;
import com.mantis.MantisNotesIterationOne.data.Article;
import com.mantis.MantisNotesIterationOne.data.ArticleBundle;
import com.mantis.MantisNotesIterationOne.databinding.FragmentDiscoverBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentDiscoverBinding binding;
    private ArticleAdapter articleAdapter = new ArticleAdapter();

    private String sampleHeadline = "At MIT, the full-time graduate studio that I administer attracts a uniquely gifted lot:\n" +
            "people who have a fundamental balance issue in the way they approach the computer as\n" +
            "an expressive medium. On the one hand, they don’t want the programming code to get\n" +
            "in the way of their designs or artistic desires; on the other hand, without hesitation they\n" +
            "write sophisticated computer codes to discover new visual pathways. The two sides of their\n" +
            "minds are in continual conﬂict. The conclusion is simple for them. Do both.";


    public DiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDiscoverBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        setupRecyclerViewAdapter();
    }

    private void setupRecyclerViewAdapter() {
        binding.discoverRecyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );
        binding.discoverRecyclerView.setAdapter( articleAdapter );
        List<ArticleBundle> articleBundles = new ArrayList<>();
        for ( int i = 0; i < 20; i++ ) {
            ArticleBundle articleBundle = new ArticleBundle();
            for ( int j = 0; j < 4; j++ )
                articleBundle.addArticle( new Article( sampleHeadline, sampleHeadline ) );
            articleBundles.add( articleBundle );
        }
        articleAdapter.setData( articleBundles );
    }
}