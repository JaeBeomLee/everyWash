package ga.washmose.mose;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.hdodenhof.circleimageview.CircleImageView;
import ga.washmose.mose.main.MainActivity;

public class MoreFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more,container, false);
        CircleImageView profile = (CircleImageView)view.findViewById(R.id.profile);
        ViewGroup moreChangeLayout = (ViewGroup)view.findViewById(R.id.more_change_layout);

        moreChangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                UserInfo.isSeller = true;
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}
