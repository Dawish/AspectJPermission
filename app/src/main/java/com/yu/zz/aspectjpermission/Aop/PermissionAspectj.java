package com.yu.zz.aspectjpermission.Aop;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.yu.zz.annotation.Permission;
import com.yu.zz.aspectjpermission.App;
import com.yu.zz.aspectjpermission.Utils.MPermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * step 3.2 此步骤，杂项较多
 *  记得Manifest加入权限 兼容低版本
 *  manifest记得加入{@link App}
 *
 *
 */
@Aspect
public class PermissionAspectj {

    @Around("execution(@com.yu.zz.annotation.Permission * *(..)) && @annotation(permission)")//在所有 有Permission的方法替换代码
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, final Permission permission) throws Throwable {
        Object targetObject = joinPoint.getTarget();
        if(targetObject instanceof Activity){
            final Activity targetActivity = (Activity) targetObject;
            MPermissionUtils.requestPermissionsResult(targetActivity, 1, permission.value()
                    , new MPermissionUtils.OnPermissionListener() {
                        @Override
                        public void onPermissionGranted() { //同意
                            try {
                                joinPoint.proceed();//获得权限，执行原方法
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onPermissionDenied() {  //拒绝
                            MPermissionUtils.showTipsDialog(targetActivity);
                        }
                    });


        }

    }
}
