package vidura.chathuranga.jobspot

data class VacancyModel(
    var vacancyId: String? = null,
    var jobPosition: String? = null,
    var typeOfWorkPlace: String? = null,
    var jobLocation: String? = null,
    var employmentType: String? = null,
    var jobDescription: String? = null,
    var companyId: String? = null,
    var isClosed: Boolean = false
)
