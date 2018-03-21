/*
 * getWorldList
 * @page:加载第几页，默认从第一页
 * @rows:一页加载多少条数据
 * */
var rows = 10,
    page = 1;
var dataList = {
    "rows": rows,
    "page": page,
};
function getWorldList() {
    $.ajax({
        url: '/getquestionByPage',
        type: 'get',
        data: dataList,
        dataType: 'json'
    }).done(function (res) {
        if (res.rtnCode === 200) {
            var r = res.bizData.map(addQuestions);
            var shit = r.reduce(function (x, y) {
                return x + y;
            });
            $('.zh-general-list').append(shit);
            $('.btn-next').text('更多').removeAttr('disabled');
        } else {
            $('.zu-main-feed-con').append("shit");
        }
    });

}

$('.btn-next').on('click', function () {
    $('.btn-next').text('加载中...').attr('disabled', 'disabled');
    dataList.page++;
    getWorldList();
});

function addQuestions(vo) {
    var name = vo.objs.user.name;
    var headUrl = vo.objs.user.headUrl;
    var questionId = vo.objs.question.id;
    var createdDate = vo.objs.question.createdDate;
    var content = vo.objs.question.content;
    var commentCount = vo.objs.question.commentCount;
    var title = vo.objs.question.title;
    var timestramp = vo.objs.question.createdDate;
    var date = new Date(timestramp);
    var html = `<div class="feed-item folding feed-item-hook feed-item-2
                        " feed-item-a="" data-type="a" id="feed-2" data-za-module="FeedItem" data-za-index="">
                            <meta itemprop="ZReactor" data-id="389034" data-meta="{&quot;source_type&quot;: &quot;promotion_answer&quot;, &quot;voteups&quot;: 4168, &quot;comments&quot;: 69, &quot;source&quot;: []}">
                            <div class="feed-item-inner">
                                <div class="avatar">
                                    <a title="${name}" data-tip="p$t$amuro1230" class="zm-item-link-avatar" target="_blank" href="https://nowcoder.com/people/amuro1230">
                                        <img src="${headUrl}" class="zm-item-img-avatar"></a>
                                </div>
                                <div class="feed-main">
                                    <div class="feed-content" data-za-module="AnswerItem">
                                        <meta itemprop="answer-id" content="389034">
                                        <meta itemprop="answer-url-token" content="13174385">
                                        <h2 class="feed-title">
                                            <a class="question_link" target="_blank" href="/question/${questionId}">${vo.objs.question.title}</a></h2>
                                        <div class="feed-question-detail-item">
                                            <div class="question-description-plain zm-editable-content"></div>
                                        </div>
                                        <div class="expandable entry-body">
                                            <div class="zm-item-vote">
                                                <a class="zm-item-vote-count js-expand js-vote-count" href="javascript:;" data-bind-votecount="">${vo.objs.followCount}</a></div>
                                            <div class="zm-item-answer-author-info">
                                                <a class="author-link" data-tip="p$b$amuro1230" target="_blank" href="/user/$!{vo.user.id}">${name}</a>
                                                ，${date.toLocaleString()})</div>
                                            <div class="zm-item-vote-info" data-votecount="4168" data-za-module="VoteInfo">
                                                <span class="voters text">
                                                    <a href="#" class="more text">
                                                        <span class="js-voteCount">${vo.objs.followCount}</span>&nbsp;人赞同</a></span>
                                            </div>
                                            <div class="zm-item-rich-text expandable js-collapse-body" data-resourceid="123114" data-action="/answer/content" data-author-name="李淼" data-entry-url="/question/19857995/answer/13174385">
                                                <div class="zh-summary summary clearfix">${content}</div>
                                            </div>
                                        </div>
                                        <div class="feed-meta">
                                            <div class="zm-item-meta answer-actions clearfix js-contentActions">
                                                <div class="zm-meta-panel">
                                                    <a data-follow="q:link" class="follow-link zg-follow meta-item" href="javascript:;" id="sfb-123114">
                                                        <i class="z-icon-follow"></i>关注问题</a>
                                                    <a href="#" name="addcomment" class="meta-item toggle-comment js-toggleCommentBox">
                                                        <i class="z-icon-comment"></i>${commentCount} 条评论</a>


                                                    <button class="meta-item item-collapse js-collapse">
                                                        <i class="z-icon-fold"></i>收起</button>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>`;
    var new_html = `<div class="Card TopstoryItem" data-reactid="82">
                            <div class="Feed" data-reactid="87" data-za-detail-view-path-module="FeedItem">
                                <div class="FeedSource" data-reactid="88">
                                    <div class="FeedSource-firstline" data-reactid="89">
                            <span data-reactid="90">
                                <span data-reactid="92"><span class="UserLink" data-reactid="93"><div class="Popover"
                                                                                                      data-reactid="94">
                                    <div id="Popover-88661-22688-toggle" aria-haspopup="true" aria-expanded="false"
                                         aria-owns="Popover-88661-22688-content" data-reactid="95">
                                        <a class="UserLink-link" data-za-detail-view-element_name="User" target="_blank"
                                           href="/user/${vo.objs.user.id}" data-reactid="96">${name}</a>
                                    </div>
                                </div>
                                </span>
                                </span>关注了问题
                            </span>
                                        <span data-reactid="99"> · </span>
                                        ${date.toLocaleString()})</div>
                                </div>
                                <div class="ContentItem" data-reactid="101" data-za-detail-view-path-module="QuestionItem">
                                    <h2 class="ContentItem-title" data-reactid="102">
                                        <div class="QuestionItem-title" data-reactid="103"><a href="/question/${questionId}"
                                                                                              target="_blank"
                                                                                              data-za-detail-view-name="Title"
                                                                                              data-reactid="104">${title}</a>
                                        </div>
                                    </h2>
                                    <div class="RichContent is-collapsed" data-reactid="105">
                                        <div class="RichContent-inner" data-reactid="106"><span
                                                class="RichText CopyrightRichText-richText" itemprop="text"
                                                data-reactid="107">${content}</span>
                                        </div>
                                        <div class="ContentItem-actions" data-reactid="114">
                                            <button class="FollowButton ContentItem-action Button--blue"
                                                    type="button" data-reactid="115"><!-- react-text: 116 -->关注问题
                                                <!-- /react-text --></button>
                                            <div class="Popover ShareMenu ContentItem-action" data-reactid="123">
                                                <div class="" id="Popover-88662-85785-toggle" aria-haspopup="true"
                                                     aria-expanded="false" aria-owns="Popover-88662-85785-content"
                                                     data-reactid="124">
                                                </div><!-- react-empty: 131 --></div>
                                            <a class="FollowButton ContentItem-action" type="button"
                                               href="/question/268533477" data-reactid="132">
                                                <!-- react-text: 137 -->${commentCount}<!-- /react-text --><!-- react-text: 138 --> 个回答
                                                <!-- /react-text --></a></div>
                                    </div><!-- react-empty: 139 --></div>
                            </div>
                        </div>`
    return new_html;
}