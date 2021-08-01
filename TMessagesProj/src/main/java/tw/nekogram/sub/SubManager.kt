package tw.nekogram.sub

import org.dizitart.no2.objects.filters.ObjectFilters
import org.telegram.messenger.LocaleController
import org.telegram.messenger.R
import tw.nekogram.database.mkDatabase

object SubManager {

    val database by lazy { mkDatabase("proxy_sub") }

    @JvmStatic
    val count
        get() = subList.find().totalCount()

    @JvmStatic
    val subList by lazy {

        database.getRepository("proxy_sub", SubInfo::class.java).apply {

            val public = find(ObjectFilters.eq("id", 1L)).firstOrDefault()

            update(SubInfo().apply {

                name = LocaleController.getString("NekoXProxy", R.string.NekoXProxy)
                enable = public?.enable ?: true

                urls = listOf(
                        "https://nekox.pages.dev/proxy_list_pro",
                        "https://github.com/NekoX-Dev/ProxyList/blob/master/proxy_list_pro@js-file-line\">@<",
                        "https://gitee.com/nekoshizuku/AwesomeRepo/raw/master/proxy_list_pro"
                )

                id = 1L
                internal = true

                proxies = public?.proxies ?: listOf()

            }, true)

        }

    }

}