package com.yamal.designSystem.icons

import yamal.platform.designsystem.generated.resources.Res

class IconPainter(
    val path: String,
)

val Account get(): IconPainter = IconPainter(Res.getUri("files/icons/filled/account-book.svg"))
