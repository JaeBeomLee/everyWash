package ga.washmose.mose;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.response.model.User;

import de.hdodenhof.circleimageview.CircleImageView;
import ga.washmose.mose.Util.UHttps;
import ga.washmose.mose.main.MainActivity;
import ga.washmose.mose.pre.LoginActivity;

public class MoreFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more,container, false);
        CircleImageView profile = (CircleImageView)view.findViewById(R.id.profile);
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView address = (TextView)view.findViewById(R.id.address);
        ViewGroup moreChangeLayout = (ViewGroup)view.findViewById(R.id.more_change_layout);
        ViewGroup logOutLayout = (ViewGroup)view.findViewById(R.id.log_out_layout);

        Glide.with(this).load(UserInfo.profileURL).into(profile);
        name.setText(UserInfo.name);
        address.setText(UserInfo.address);
        moreChangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                UserInfo.isSeller = true;
                startActivity(intent);
                getActivity().finish();
            }
        });

        logOutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserInfo.loginType == UserInfo.TYPE_FACEBOOK){
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }else if (UserInfo.loginType == UserInfo.TYPE_KAKAO){
                    UserManagement.requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
                }
            }
        });
        return view;
    }
}
