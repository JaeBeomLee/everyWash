package ga.washmose.mose;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.response.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import ga.washmose.mose.User.UserLaundryFragment;
import ga.washmose.mose.Util.UHttps;
import ga.washmose.mose.main.MainActivity;
import ga.washmose.mose.pre.LoginActivity;
import ga.washmose.mose.seller.SellerOrderRequestFragment;

import static ga.washmose.mose.UserInfo.isSeller;

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
        TextView changeBtnText = (TextView)view.findViewById(R.id.more_change_text);
        ViewGroup moreChangeLayout = (ViewGroup)view.findViewById(R.id.more_change_layout);
        ViewGroup logOutLayout = (ViewGroup)view.findViewById(R.id.log_out_layout);

        Glide.with(this).load(UserInfo.profileURL).into(profile);
        name.setText(UserInfo.name);
        address.setText(UserInfo.address);

        if (UserInfo.isSeller){
            changeBtnText.setText("사용자로 변경하기");
        }else{
            changeBtnText.setText("판매자로 변경하기");
        }
        moreChangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSellerBeen();
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

    public void isSellerBeen(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int code;
                JSONObject res = UHttps.okHttp(UHttps.IP+"/v1/seller/checkinit", UserInfo.apiKey);
                if (res != null){
                    code = res.optInt("code");
                    if (code == 200){
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        UserInfo.isSeller = !UserInfo.isSeller;
                        startActivity(intent);
                        getActivity().finish();
                    }else{
                        Intent intent = new Intent(getContext(), RegisterSellerActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                }else {

                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }
}
