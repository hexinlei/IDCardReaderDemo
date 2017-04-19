// IDManager.aidl
package com.ranger.aidl;
import com.ranger.aidl.IDCardInfoData;
// Declare any non-default types here with import statements

interface IDManager {

        String getCheckResult(in IDCardInfoData info);

}
