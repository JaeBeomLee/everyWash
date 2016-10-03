package ga.washmose.mose.pre;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ga.washmose.mose.R;

public class LoginFragment extends Fragment{
    private static final String ARG_SECTION_NUMBER = "section_number";
    int position;
    public LoginFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_SECTION_NUMBER);
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */


    public static LoginFragment newInstance(int sectionNumber) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        TextView text = (TextView) rootView.findViewById(R.id.login_text);
        ImageView background = (ImageView) rootView.findViewById(R.id.login_background);
        ImageView icon = (ImageView) rootView.findViewById(R.id.login_icon);


        switch (position){
            case 0:
                background.setImageResource(R.drawable.login_step_1);
                icon.setImageResource(R.drawable.ic_login_step_1);
                text.setText(R.string.login_step_1);
                break;
            case 1:
                background.setImageResource(R.drawable.login_step_2);
                icon.setImageResource(R.drawable.ic_login_step_2);
                text.setText(R.string.login_step_2);
                break;
            case 2:
                background.setImageResource(R.drawable.login_step_3);
                icon.setImageResource(R.drawable.ic_logo);
                text.setText(R.string.login_step_3);
                break;
        }
        return rootView;
    }




}
