package adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.justshare.ImagesFragment;
import com.example.justshare.fragments.ApksFragment;
import com.example.justshare.fragments.ContactsFragment;
import com.example.justshare.fragments.FilesFragment;
import com.example.justshare.fragments.VideosFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private int num_tabs;
    public ViewPagerAdapter(FragmentActivity fragmentActivity, int num){
        super(fragmentActivity);
        this.num_tabs = num;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ImagesFragment();
            case 1:
                return new FilesFragment();
            case 2:
                return new VideosFragment();
            case 3:
                return new ApksFragment();
            case 4:
                return new ContactsFragment();
        }
        return new ImagesFragment();

    }

    @Override
    public int getItemCount(){
        return num_tabs;
    }
}
