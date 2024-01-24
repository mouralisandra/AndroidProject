package com.example.ProjetAndroid.Model

import com.example.ProjetAndroid.Model.database.dbmodels.UserDbModel
import com.example.ProjetAndroid.Model.database.dbmodels.VnAdditionalInfoDbModel
import com.example.ProjetAndroid.Model.database.dbmodels.VnBasicInfoDbModel
import com.example.ProjetAndroid.Model.database.dbmodels.VnFullInfoDbModel
import com.example.ProjetAndroid.Model.api.dataclass.Authinfo
import com.example.ProjetAndroid.Model.api.dataclass.visualnovel.VnResults
import com.example.ProjetAndroid.Model.DTOs.ScreenshotList
import com.example.ProjetAndroid.Model.DTOs.User
import com.example.ProjetAndroid.Model.DTOs.Vn
import javax.inject.Inject

class VnMapper @Inject constructor() {

    fun mapBasicDbModelInfoToEntity(vnBasicInfoDbModel: VnBasicInfoDbModel) = Vn(
        id = vnBasicInfoDbModel.id,
        image = vnBasicInfoDbModel.image,
        rating = vnBasicInfoDbModel.rating,
        title = vnBasicInfoDbModel.title,
        description = "",
        tags = listOf(),
        screenshots = listOf()
    )

    fun mapFullInfoToEntity(fullInfo: VnFullInfoDbModel) = Vn(
        id = fullInfo.vnBasicInfoDbModel.id,
        image = fullInfo.vnBasicInfoDbModel.image,
        rating = fullInfo.vnBasicInfoDbModel.rating,
        title = fullInfo.vnBasicInfoDbModel.title,
        description = fullInfo.vnAdditionalInfoDbModel.description,
        tags = fullInfo.vnAdditionalInfoDbModel.tags,
        screenshots = fullInfo.vnAdditionalInfoDbModel.screenshots
    )


    fun mapAuthInfoToUserDbModel(authinfo: Authinfo) = UserDbModel(
        id = authinfo.id,
        username = authinfo.username
    )

    fun mapUserDbModelToUser(userDbModel: UserDbModel?) = if (userDbModel == null) null else User(
        id = userDbModel.id,
        username = userDbModel.username
    )

    fun mapVnResponseToBasicDbModelInfo(vnResults: VnResults) = VnBasicInfoDbModel(
        id = vnResults.id,
        image = vnResults.image.url,
        rating = vnResults.rating,
        votecount = vnResults.votecount,
        title = vnResults.title
    )

    fun mapListVnResponseToListBasicDbModelInfo(list: List<VnResults>) = list.map {
        mapVnResponseToBasicDbModelInfo(it)
    }

    fun mapVnResponseToAdditionalDbModelInfo(vnResults: VnResults) = VnAdditionalInfoDbModel(
        id = vnResults.id,
        description = vnResults.description,
        tags = vnResults.tags,
        screenshots = buildList {
            vnResults.screenshots.groupBy { it.release.id }.forEach { s, screenshots ->
                add(ScreenshotList(
                    title = screenshots.first().release.title,
                    releaseId = screenshots.first().release.id,
                    screenshotList = buildList {
                        screenshots.forEach {
                            add(Pair(it.thumbnail, it.sexual))
                        }
                    }
                ))
            }
        }
    )
}