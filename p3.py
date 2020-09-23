import urllib3 as ulib
import json
import pandas as pd
import numpy as np
import re


class WxIdeaInsight(object):
    def __init__(self):
        self.uid = self.pwd = ''
        self.http = ulib.PoolManager()
        self.context = None

    def login(self, uid, pwd):
        self.uid = '#####' #uid
        self.pwd = '#####' #pwd
        login_url = u'https://login.huawei.com/login/login.do'
        params = {'actionFlag': "loginAuthenticate", "lang": "en",
                  "loginMethod": "login", "loginPageType": "mix",
                  "redirect": "http%3A%2F%2Fplm-idea.huawei.com%2Fidea%2FdetailsPreview.html%3FideaId%3D30057%26details%3Dtrue",
                  "redirect_local": "", "redirect_modify": "", "scanedFinPrint": "",
                  'uid': self.uid, 'password': self.pwd, 'verifyCode': '2345'}
        self.context = self.http.request('POST', login_url, fields=params)
        print(self.context.status)

    def get_idea_score(self, idea_id):
        cookies = self.context.getheader('set-cookie')
        score_url = u'http://plm-idea.huawei.com/services/my/review/getRecentIdeaReview?ideaId=%s&campaignId=4981' % idea_id
        print(score_url)
        resp = self.http.request('GET', score_url, headers={'cookie': cookies})
        #print(resp.status)
        jscore = json.loads(resp.data.decode('utf-8'))
        # print(idea_id, jscore)
        data = dict(jscore)['data']
        if not data:
            return
        return data['avgScore']
        # # print(score)
        # return score

    def get_wx_ideas(self):
        def get_ideas_info_alpha():
            url = u'http://plm-idea.huawei.com/services/idea/idea/queryIdeaOrder/rank/3000/1?campaignId=4981'
            cookies = self.context.getheader('set-cookie')
            resp = self.http.request('GET', url, headers={'cookie': cookies})
            jdata = dict(json.loads(resp.data.decode('utf-8')))
            # total, curpage = jdata['totalCount'], jdata['curPage']
            idea_list = jdata['data']
			
            def remove_control_chars(s):
                control_chars = ''.join(map(chr, list(range(0,32)) + list(range(127,160))))
                control_char_re = re.compile('[%s]' % re.escape(control_chars))
                return control_char_re.sub('', s)


            def get_idea_id(idea):
                return remove_control_chars(str(idea['id']))

            def get_idea_title(idea):
                return remove_control_chars(idea['title'] + '')

            def get_idea_uid(idea):
                return remove_control_chars(idea['creator']['nickname'] + '')
				
            # return [get_idea_id(i) for i in idea_list], [get_idea_title(j) for j in idea_list],

            i = []
            t = []
            u = []
            per = 0
            for k in idea_list:
                i.append(get_idea_id(k))
                t.append(get_idea_title(k))
                u.append(get_idea_uid(k))				
                print('progress:%d%%' % (per * 100 / len(idea_list)))
                per = per + 1
            return i, t, u

        id_list, title_list, uid_list = get_ideas_info_alpha()
        # print(id_list)
        # print(title_list)
        score_list = [self.get_idea_score(id) for id in id_list]
        return id_list, title_list, uid_list, score_list
        # return id_list,title_list,[]


def get_department_ideas(f):
    df = pd.read_excel(f, u'Sheet1')
    links = df['link']

    # print(len(links))
    # print(df['link'][0])

    def get_ideaid(link):
        b = str(link).find('=')
        e = link.find('&')
        return link[b + 1:e]

    return [get_ideaid(i) for i in links], df


def get_department_ideas_scores():
    def get_ideas_scores(ideas):
        ii = WxIdeaInsight()
        ii.login('{w3username}', '{password}')
        # print(ideas)
        scores = [ii.get_idea_score(i) for i in ideas]
        # print(scores)
        return scores

    idea_file = u'G:/spider/python_idea/wxcyds.xlsx'
    ideas, df = get_department_ideas(idea_file)
    scores = get_ideas_scores(ideas)
    df['Score'] = scores
    df1 = df.sort(columns='Score', ascending=False)
    # print(df1)
    df1.to_csv('1.csv')


def get_top100_ideas_score():
    def get_ideas_scores(ideas):
        ii = WxIdeaInsight()
        ii.login('{w3username}', '{password}')
        # print(ideas)
        scores = [ii.get_idea_score(i) for i in ideas]
        # print(scores)
        return scores

    def get_top100_ideas(f):
        df0 = pd.read_excel(f, u'Sheet1')
        ideas = df0['ID']
        return [i for i in ideas], df0

    idea_file=u'G:/spider/python_idea/TOP100.xlsx'
    idealist, df = get_top100_ideas(idea_file)
    print(idealist)
    scores = get_ideas_scores(idealist)
    df['AvgScore'] = scores
    df1 = df.sort_values('AvgScore', ascending=False)#df1 = df#.sort(columns='AvgScore', ascending=False)
    print(df)
    df1.to_csv('100.csv')

# get_department_ideas_scores()



def get_wx_scores():
    ii = WxIdeaInsight()
    ii.login('{w3username}', '{password}')
    d, t, u, s = ii.get_wx_ideas()
    #print(i)
    print(t)
    print(s)
    df = pd.DataFrame()
    df['id'] = d
    df['uid'] = u
    df['name'] = t
    df['score'] = s

    def add_links(id_list):
        link_fmt = u'http://plm-idea.huawei.com/idea/detailsPreview.html?ideaId=%s&details=true'
        link_list = [link_fmt % i for i in id_list]
        return link_list

    df['link'] = add_links(d)
    df1 = df.sort_values('score', ascending=False)
	#df1 = df.sort(columns='score', ascending=False)
    df1.to_excel('6.xlsx')


def add_links():
    f = u'G:/spider/python_idea/3-1.csv'
    df = pd.read_csv(f, encoding='utf8')
    idea_list = df['id']
    link_fmt = u'http://plm-idea.huawei.com/idea/detailsPreview.html?ideaId=%s&details=true'
    link_list = [link_fmt % i for i in idea_list]
    df['link'] = link_list
    df.to_csv('4.csv')
    df.to_excel('5.xlsx')


# test()
#add_links()

# get_wx_scores()
#get_top100_ideas_score()
#get_department_ideas_scores()
get_wx_scores()
